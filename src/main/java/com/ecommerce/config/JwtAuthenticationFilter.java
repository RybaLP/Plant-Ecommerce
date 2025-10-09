package com.ecommerce.config;

import com.ecommerce.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String accessToken = null;
        String clientEmail = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);

            try {
                clientEmail = jwtService.extractEmail(accessToken);
            } catch (io.jsonwebtoken.ExpiredJwtException expiredEx) {
                Cookie[] cookies = request.getCookies();
                String refreshToken = null;

                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if ("refreshToken".equals(c.getName())) {
                            refreshToken = c.getValue();
                        }
                    }
                }

                if (refreshToken != null) {
                    try {
                        if (!jwtService.isTokenExpired(refreshToken)) {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(
                                    jwtService.extractEmail(refreshToken)
                            );

                            accessToken = jwtService.generateAccessTokenFromRefresh(refreshToken);
                            clientEmail = userDetails.getUsername();

                            response.setHeader("Authorization", "Bearer" + accessToken);
                        } else {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh token expired");
                            return;
                        }
                    } catch (Exception e) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired and no refresh token found");
                }
            }
        }

        if (clientEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(clientEmail);
            if (jwtService.isTokenValid(accessToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}