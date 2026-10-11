package com.example.rbac.dto;

import java.util.Set;

public record AuthResponse(String token, String tokenType, Long id, String fullName,
                           String email, Set<String> roles, Set<String> permissions) {}
