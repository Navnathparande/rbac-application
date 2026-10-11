package com.example.rbac.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Permission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 80)
    private String code;
    @Column(nullable = false, length = 150)
    private String description;

    protected Permission() {}
    public Permission(String code, String description) { this.code = code; this.description = description; }
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public void setCode(String code) { this.code = code; }
    public void setDescription(String description) { this.description = description; }
}
