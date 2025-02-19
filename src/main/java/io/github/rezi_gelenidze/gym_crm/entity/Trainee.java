package io.github.rezi_gelenidze.gym_crm.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Trainee extends User {
    private String dateOfBirth;
    private String address;

    public Trainee(Long userId, String firstName, String lastName, String username, String password, String dateOfBirth, String address) {
        // User base fields
        super(userId, firstName, lastName, username, password, true);

        // Trainee fields
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}
