package com.jovana.notesbynona.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovana.notesbynona.repository.RefreshTokenRepository;
import com.jovana.notesbynona.service.AuthService;
import com.jovana.notesbynona.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String refreshToken = request.getParameter("refreshToken");
        if (!jwtService.refreshTokenExists(refreshToken)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Create a response object
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Invalid refresh token");

            // Write the response object to the HttpServletResponse
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
            response.getWriter().flush();
            return;
        }


        authService.logout(refreshToken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Create a response object
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logout successful");

        // Write the response object to the HttpServletResponse
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        response.getWriter().flush();
    }

}
