package com.ecommerce.service;

import com.ecommerce.dto.auth.AuthenticationResponseDto;
import com.ecommerce.model.user.Client;
import com.ecommerce.model.user.User;
import com.ecommerce.repository.user.ClientRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final ClientRepository clientRepository;

    @Value("${jwt.secret}")
    private String secretKey;

//    extract
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

//    generation
    public String generateToken (UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken (Map<String, Object> extraClaims, UserDetails userDetails ) {

//
        if (userDetails instanceof com.ecommerce.model.user.User user) {
            extraClaims.put("role", user.getRole().name());
        }
//

        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken (UserDetails userDetails) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(7,ChronoUnit.DAYS)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateAccessTokenFromRefresh (String refreshToken) {
        Client client = clientRepository.findByEmail(extractEmail(refreshToken))
                .orElseThrow(()-> new IllegalStateException("User not found"));

        return generateToken(client);
    }


//    refresh

    public AuthenticationResponseDto refreshAccessToken (String refreshToken) {

        Client client = clientRepository.findByEmail(extractEmail(refreshToken))
                .orElseThrow(() -> new IllegalStateException("Could not find user email"));

        if (!isTokenValid(refreshToken, client)) {
            throw new IllegalStateException("Token is invalid or expired");
        }

        String newAccessToken = generateToken(client);

        return AuthenticationResponseDto.builder()
                .token(newAccessToken)
                .build();

    }


//    validation
    public boolean isTokenValid (String token , UserDetails userDetails) {
        final String clientEmail = extractEmail(token);
        return (clientEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired (String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration (String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims (String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getBody();
    }


//    key
    private SecretKey getSignInKey () {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}