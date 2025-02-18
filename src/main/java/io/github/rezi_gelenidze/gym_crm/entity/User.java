package io.github.rezi_gelenidze.gym_crm.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive = true;
}
