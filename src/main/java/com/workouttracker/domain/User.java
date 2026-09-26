package com.workouttracker.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,unique = true,length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false,name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate(){
        if (createdAt == null) createdAt = Instant.now();
    }


}
