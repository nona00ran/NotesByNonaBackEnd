package com.jovana.notesbynona.controller;

import com.jovana.notesbynona.model.login.LoginRequest;
import com.jovana.notesbynona.model.login.LoginResponse;
import com.jovana.notesbynona.service.AuthService;
import com.jovana.notesbynona.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/tokenTest")
    public ResponseEntity<Claims> tokenTest(@RequestBody String token) {
        return ResponseEntity.ok(jwtService.verifyAndParseToken(token));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestParam String refreshToken) {
        LoginResponse response = authService.refresh(refreshToken);
        return ResponseEntity.ok(response);
    }

}
