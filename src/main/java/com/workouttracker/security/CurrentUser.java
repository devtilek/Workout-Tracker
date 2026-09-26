package com.workouttracker.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUser {
    public UUID id(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UUID userId)){
            throw new IllegalArgumentException("No authenticated user in context");
        }
        return userId;
    }

}
