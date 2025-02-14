package io.github.rezi_gelenidze.gym_crm.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// Avoid boilerplate code by using Lombok annotations
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    // Define attributes
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
}
