package com.jovana.notesbynona.service.impl;

import com.jovana.notesbynona.model.login.LoginRequest;
import com.jovana.notesbynona.model.login.LoginResponse;
import com.jovana.notesbynona.entity.User;
import com.jovana.notesbynona.repository.RefreshTokenRepository;
import com.jovana.notesbynona.service.AuthService;
import com.jovana.notesbynona.service.JwtService;
import com.jovana.notesbynona.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> user = userService.findByUsername(loginRequest.getUsername().toLowerCase());
        if (user.isEmpty()) {
            throw new BadCredentialsException("Invalid username");
        }
        User foundUser = user.get();

        boolean isCorrectPassword = userService.verifyPassword(loginRequest.getPassword(), foundUser.getPassword());
        if (!isCorrectPassword) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = jwtService.createToken(foundUser);
        String refreshToken = jwtService.createRefreshToken(foundUser, null);
        return new LoginResponse(token, refreshToken, "Login successful");
    }

    public LoginResponse refresh(String refreshToken) {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw new JwtException("Invalid refresh token");
        }
        User user = refreshTokenRepository.findByToken(refreshToken).get().getUser();
        refreshTokenRepository.deleteByToken(refreshToken);
        String token = jwtService.createToken(user);
        return new LoginResponse(token, jwtService.createRefreshToken(user, null), "Token refreshed successfully");
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    @PostConstruct
    public void deleteExpiredTokensOnStartup() {
        deleteExpiredTokens();
    }

    @Scheduled(cron = "0 0 0 * * SUN") // Runs every Sunday at midnight
    public void deleteExpiredTokensWeekly() {
        deleteExpiredTokens();
    }

    private void deleteExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredTokens();
    }
}
