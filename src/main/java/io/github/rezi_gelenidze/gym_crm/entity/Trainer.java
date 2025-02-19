package io.github.rezi_gelenidze.gym_crm.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Trainer extends User {
    private String specialization;

    public Trainer(Long userId, String firstName, String lastName, String username, String password, String specialization) {
        // User base fields
        super(userId, firstName, lastName, username, password, true);

        // Trainer fields
        this.specialization = specialization;
    }
}