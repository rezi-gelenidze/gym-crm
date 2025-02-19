package io.github.rezi_gelenidze.gym_crm.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public abstract class User {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive = true;

    public User(Long userId, String firstName, String lastName, String username, String password, boolean isActive) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }
}
