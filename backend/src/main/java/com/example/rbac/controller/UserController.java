package com.example.rbac.controller;

import com.example.rbac.dto.UserResponse;
import com.example.rbac.entity.AppUser;
import com.example.rbac.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository users;
    public UserController(UserRepository users) { this.users = users; }

    @GetMapping("/me")
    public UserResponse me(org.springframework.security.core.Authentication authentication) {
        AppUser user = users.findByEmailIgnoreCase(authentication.getName()).orElseThrow();
        return toResponse(user);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ') or hasRole('ADMIN')")
    public List<UserResponse> all() { return users.findAll().stream().map(this::toResponse).toList(); }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('USER_MANAGE') or hasRole('ADMIN')")
    public UserResponse setStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        AppUser user = users.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(enabled);
        return toResponse(users.save(user));
    }

    private UserResponse toResponse(AppUser u) {
        Set<String> roles = u.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet());
        Set<String> permissions = u.getRoles().stream().flatMap(r -> r.getPermissions().stream())
          .map(p -> p.getCode()).collect(Collectors.toSet());
        return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.isEnabled(), roles, permissions);
    }
}
