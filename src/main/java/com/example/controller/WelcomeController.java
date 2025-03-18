package com.example.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome(HttpSession session, Principal principal, Model model) {
        Map<String, Object> sessionInfo = new LinkedHashMap<>();
        sessionInfo.put("Session ID", session.getId());
        sessionInfo.put("Username", principal.getName());
        sessionInfo.put("Creation Time", new Date(session.getCreationTime()));
        sessionInfo.put("Last Accessed Time", new Date(session.getLastAccessedTime()));
        sessionInfo.put("Max Inactive Interval", session.getMaxInactiveInterval() + " seconds");

        model.addAttribute("sessionInfo", sessionInfo);
        return "welcome";
    }
} 