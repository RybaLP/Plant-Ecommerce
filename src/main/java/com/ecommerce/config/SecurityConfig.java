package com.ecommerce.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("http://localhost:5173"));
                    corsConfig.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))

                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/refresh").hasRole("CLIENT")
                        .requestMatchers("/api/auth/**", "/api/plant/**", "/api/plant", "/api/stripe/webhook", "/api/orders/guest",
                                "/api/user-password/reset-password", "/api/user-password", "/api/orders/check", "/api/review/plant/*")
                        .permitAll()
                        .requestMatchers("/", "/error", "/webjars/**",
                                "/oauth2/**", "/login/oauth2/**").permitAll()
                        .requestMatchers("/api/orders/test").hasRole("CLIENT")
                        .requestMatchers("/api/orders/**").hasRole("CLIENT")
                        .requestMatchers("/api/cart/**").hasRole("CLIENT")
                        .requestMatchers("/api/review/**").hasRole("CLIENT")
                        .requestMatchers("/api/payment/checkout").hasRole("CLIENT")
                        .requestMatchers("/api/address").hasRole("CLIENT")
                        .anyRequest()
                        .authenticated()
                )
                .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2LoginSuccessHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}