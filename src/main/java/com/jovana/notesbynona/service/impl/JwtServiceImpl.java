package com.jovana.notesbynona.service.impl;

import com.jovana.notesbynona.entity.RefreshToken;
import com.jovana.notesbynona.entity.User;
import com.jovana.notesbynona.repository.RefreshTokenRepository;
import com.jovana.notesbynona.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.crypto.SecretKey;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh-expiration-time}")
    private long refreshExpiration;

    private final RefreshTokenRepository refreshTokenRepository;


    public String createToken(User user) {
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("username", user.getUsername());
        tokenData.put("userId", user.getId());
        tokenData.put("authorities", user.getAuthoritiesAsStrings());
        tokenData.put("issuedAt", new Date());

        SecretKey key = getSignInKey();
        return Jwts.builder().signWith(key).claims(tokenData).expiration(new Date(System.currentTimeMillis() + jwtExpiration)).compact();
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public Claims verifyAndParseToken(String token) {
        Jwt<?, ?> jwt;
        try {
            // Note: This will also throw an exception if the token is expired
            jwt = Jwts.parser().verifyWith(getSignInKey()).build().parse(token);
        } catch (JwtException ex) {
            throw new JwtException("Invalid JWT token", ex);
        }
        return (Claims) jwt.getPayload();
    }

    public String createRefreshToken(User user, String hashedFingerprint) {
        SecretKey key = getSignInKey();
        RefreshToken token = new RefreshToken();
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("username", user.getUsername());
        tokenData.put("userId", user.getId());
        tokenData.put("fingerprint", hashedFingerprint);
        tokenData.put("issuedAt", new Date());
        Instant expiration = Instant.now().plusMillis(refreshExpiration);

        token.setToken(Jwts.builder().expiration(Date.from(expiration)).signWith(key).claims(tokenData).compact());

        token.setUser(user);
        token.setExpiryDate(expiration);
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isPresent()){
            return refreshToken.get().getExpiryDate().isAfter(Instant.now());
        } else {
            return false;
        }
    }

    public boolean refreshTokenExists(String token) {
        return refreshTokenRepository.findByToken(token).isPresent();
    }

}
