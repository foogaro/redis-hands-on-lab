package com.example.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AuthenticationEventHandlers implements AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEventHandlers.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        String timestamp = LocalDateTime.now().format(formatter);
        String sessionId = request.getSession().getId();
        
        logger.info("User '{}' successfully logged in at {} (Session ID: {})", username, timestamp, sessionId);
        response.sendRedirect("/welcome");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        String timestamp = LocalDateTime.now().format(formatter);
        
        logger.warn("Failed login attempt for user '{}' at {} - Reason: {}", 
                   username, timestamp, exception.getMessage());
        response.sendRedirect("/login?error");
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                               Authentication authentication) throws IOException, ServletException {
        String username = authentication != null ? authentication.getName() : "unknown";
        String timestamp = LocalDateTime.now().format(formatter);
        
        logger.info("User '{}' successfully logged out at {}", username, timestamp);
        response.sendRedirect("/login?logout");
    }
} 