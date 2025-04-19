package com.jovana.notesbynona.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jovana.notesbynona.model.error.APIErrorResponse;
import com.jovana.notesbynona.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    private void writeErrorResponse(HttpServletResponse response, APIErrorResponse apiErrorResponse) throws IOException {
        response.setStatus(apiErrorResponse.getStatus());
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        objectMapper.registerModule(javaTimeModule);
        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
    }

    private void authenticateUser(String username, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                Claims claims = jwtService.verifyAndParseToken(token);
                String username = claims.get("username", String.class);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    authenticateUser(username, request);
                }

            } catch (JwtException ex) {
                APIErrorResponse apiErrorResponse = new APIErrorResponse();
                apiErrorResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                apiErrorResponse.setError("Unauthorized");
                apiErrorResponse.setMessage("Unable to verify and parse token: " + ex.getMessage());
                apiErrorResponse.setTimestamp(LocalDateTime.now());
                writeErrorResponse(response, apiErrorResponse);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}