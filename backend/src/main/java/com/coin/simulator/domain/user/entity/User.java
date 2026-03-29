package com.coin.simulator.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private String role;
    private LocalDateTime createdAt;

    @Builder
    public User(String username, String password, String role, LocalDateTime createdAt) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
}
