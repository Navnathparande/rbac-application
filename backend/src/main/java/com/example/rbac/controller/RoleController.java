
package com.example.rbac.controller;

import com.example.rbac.entity.Role;
import com.example.rbac.entity.Permission;
import com.example.rbac.repository.RoleRepository;
import com.example.rbac.repository.PermissionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleRepository roles;
    private final PermissionRepository permissions;

    public RoleController(RoleRepository roles,
                          PermissionRepository permissions) {
        this.roles = roles;
        this.permissions = permissions;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_READ')")
    public List<RoleView> all() {
        return roles.findAll().stream()
                .map(r -> new RoleView(
                        r.getId(),
                        r.getName(),
                        r.getPermissions().stream()
                                .map(Permission::getCode)
                                .sorted()
                                .toList()))
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_MANAGE')")
    public RoleView create(@RequestBody CreateRoleRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Role name is required");
        }

        String name = request.name().trim().toUpperCase();

        if (roles.findByName(name).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Role already exists");
        }

        Role role = roles.save(new Role(name));
        return toView(role);
    }

    @PutMapping("/{id}/permissions")
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE_MANAGE')")
    public RoleView updatePermissions(
            @PathVariable Long id,
            @RequestBody UpdatePermissionsRequest request) {

        Role role = roles.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Role not found"));

        if (request.permissionCodes() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "permissionCodes is required");
        }

        Set<Permission> selected = new HashSet<>();

        for (String code : request.permissionCodes()) {
            if (code == null || code.isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Permission code cannot be blank");
            }

            Permission permission = permissions.findByCode(
                            code.trim().toUpperCase())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Unknown permission: " + code));

            selected.add(permission);
        }

        role.getPermissions().clear();
        role.getPermissions().addAll(selected);

        Role saved = roles.save(role);
        return toView(saved);
    }

    private RoleView toView(Role role) {
        return new RoleView(
                role.getId(),
                role.getName(),
                role.getPermissions().stream()
                        .map(Permission::getCode)
                        .sorted()
                        .toList());
    }

    public record CreateRoleRequest(String name) {}

    public record UpdatePermissionsRequest(
            List<String> permissionCodes) {}

    public record RoleView(
            Long id,
            String name,
            List<String> permissions) {}
}
