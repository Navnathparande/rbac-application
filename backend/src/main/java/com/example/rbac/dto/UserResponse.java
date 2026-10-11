package com.example.rbac.dto;

import java.util.Set;

public record UserResponse(Long id, String fullName, String email, boolean enabled,
                           Set<String> roles, Set<String> permissions) {}
