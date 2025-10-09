package com.ecommerce.controller;

import com.ecommerce.dto.auth.AuthenticationResponseDto;
import com.ecommerce.dto.auth.LoginRequestDto;
import com.ecommerce.dto.auth.RegisterRequestDto;
import com.ecommerce.dto.auth.TokenRefreshRequest;
import com.ecommerce.service.AuthenticationService;
import com.ecommerce.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController{

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> registerUser(
            @Valid @RequestBody RegisterRequestDto registerRequestDto, HttpServletResponse httpServletResponse ) {
        AuthenticationResponseDto authenticationResponseDto = authenticationService.registerUser(registerRequestDto);

        Cookie cookie = new Cookie("refreshToken" , authenticationResponseDto.getRefreshToken());

//        cookie config
        cookie.setHttpOnly(true);

//        set false for development
        cookie.setSecure(false);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setAttribute("SameSite", "Strict");

        httpServletResponse.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AuthenticationResponseDto.builder()
                        .token(authenticationResponseDto.getToken())
                        .build());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticateUser (@Valid @RequestBody LoginRequestDto loginRequestDto
    , HttpServletResponse response) {
        AuthenticationResponseDto authenticationResponseDto = authenticationService.authenticate(loginRequestDto);

        Cookie cookie = new Cookie("refreshToken" , authenticationResponseDto.getRefreshToken());

//        cookie configuration
        cookie.setHttpOnly(true);

//        set false for development
        cookie.setSecure(false);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setAttribute("SameSite", "Strict");

        response.addCookie(cookie);


        return ResponseEntity.ok(
                AuthenticationResponseDto.builder()
                        .token(authenticationResponseDto.getToken())
                        .build()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponseDto> refreshAccessToken (HttpServletRequest httpServletRequest) {

        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            throw new IllegalStateException("No cookies found in request");
        }

        String refreshToken = null;

        for (Cookie c : cookies) {
            if (c.getName().equals("refreshToken")) {
                refreshToken = c.getValue();
            }
        }

        if (refreshToken == null) {
            throw new IllegalStateException("Refresh token not found in cookies");
        }

        AuthenticationResponseDto authenticationResponseDto = jwtService.refreshAccessToken(refreshToken);

        return ResponseEntity.ok(authenticationResponseDto);
    }
}
