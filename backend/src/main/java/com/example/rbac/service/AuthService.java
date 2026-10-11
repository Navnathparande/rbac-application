package com.example.rbac.service;

import com.example.rbac.dto.*;
import com.example.rbac.entity.*;
import com.example.rbac.repository.*;
import com.example.rbac.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwt;

    public AuthService(UserRepository users, RoleRepository roles, PasswordEncoder encoder,
                       AuthenticationManager authenticationManager, JwtService jwt) {
        this.users=users; this.roles=roles; this.encoder=encoder;
        this.authenticationManager=authenticationManager; this.jwt=jwt;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email)) throw new IllegalArgumentException("Email is already registered");
        AppUser user = new AppUser(request.fullName().trim(), email, encoder.encode(request.password()));
        Role role = roles.findByName("EMPLOYEE")
          .orElseThrow(() -> new IllegalStateException("Default role has not been initialized"));
        user.getRoles().add(role);
        users.save(user);
        return response(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
          request.email().trim().toLowerCase(), request.password()));
        AppUser user = users.findByEmailIgnoreCase(request.email().trim())
          .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        return response(user);
    }

    private AuthResponse response(AppUser user) {
        Set<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        Set<String> permissions = user.getRoles().stream().flatMap(r -> r.getPermissions().stream())
          .map(Permission::getCode).collect(Collectors.toSet());
        return new AuthResponse(jwt.generate(user), "Bearer", user.getId(), user.getFullName(),
          user.getEmail(), roleNames, permissions);
    }
}
