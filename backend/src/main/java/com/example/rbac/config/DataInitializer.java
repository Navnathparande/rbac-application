package com.example.rbac.config;

import com.example.rbac.entity.*;
import com.example.rbac.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seed(PermissionRepository permissions, RoleRepository roles,
                           UserRepository users, PasswordEncoder encoder,
                           @Value("${app.admin.email:}") String adminEmail,
                           @Value("${app.admin.password:}") String adminPassword) {
        return args -> seedData(permissions, roles, users, encoder, adminEmail, adminPassword);
    }

    @Transactional
    void seedData(PermissionRepository permissions, RoleRepository roles, UserRepository users,
                  PasswordEncoder encoder, String adminEmail, String adminPassword) {
        Map<String,String> defaults = Map.of(
          "USER_READ","View users", "USER_MANAGE","Activate or deactivate users",
          "ROLE_READ","View roles", "ROLE_MANAGE","Manage roles and permissions",
          "AUDIT_READ","View audit logs");
        defaults.forEach((code, desc) -> permissions.findByCode(code)
          .orElseGet(() -> permissions.save(new Permission(code, desc))));
        Role employee = roles.findByName("EMPLOYEE").orElseGet(() -> roles.save(new Role("EMPLOYEE")));
        Role manager = roles.findByName("MANAGER").orElseGet(() -> roles.save(new Role("MANAGER")));
        Role admin = roles.findByName("ADMIN").orElseGet(() -> roles.save(new Role("ADMIN")));
        manager.getPermissions().add(permissions.findByCode("USER_READ").orElseThrow());
        admin.getPermissions().addAll(permissions.findAll());
        roles.save(employee); roles.save(manager); roles.save(admin);

        if (adminEmail != null && !adminEmail.isBlank() && adminPassword != null && !adminPassword.isBlank()
            && !users.existsByEmailIgnoreCase(adminEmail)) {
            AppUser user = new AppUser("System Administrator", adminEmail.trim().toLowerCase(),
              encoder.encode(adminPassword));
            user.getRoles().add(admin);
            users.save(user);
        }
    }
}
