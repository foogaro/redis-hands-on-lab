package com.example.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SessionController {

    @GetMapping("/session-info")
    public Map<String, Object> getSessionInfo(HttpSession session, Principal principal) {
        Map<String, Object> sessionInfo = new HashMap<>();
        sessionInfo.put("sessionId", session.getId());
        sessionInfo.put("creationTime", session.getCreationTime());
        sessionInfo.put("lastAccessedTime", session.getLastAccessedTime());
        sessionInfo.put("maxInactiveInterval", session.getMaxInactiveInterval());
        sessionInfo.put("username", principal.getName());
        return sessionInfo;
    }

    @PostMapping("/session-attribute")
    public void setSessionAttribute(
            @RequestParam String key,
            @RequestParam String value,
            HttpSession session) {
        session.setAttribute(key, value);
    }

    @GetMapping("/session-attribute")
    public Object getSessionAttribute(
            @RequestParam String key,
            HttpSession session) {
        return session.getAttribute(key);
    }
} 