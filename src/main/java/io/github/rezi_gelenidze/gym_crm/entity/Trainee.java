package io.github.rezi_gelenidze.gym_crm.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "trainees")
public class Trainee extends User {
    @Column(nullable = false)
    private String dateOfBirth;

    @Column(nullable = false)
    private String address;

    public Trainee(Long userId, String firstName, String lastName, String username, String password, boolean active, String dateOfBirth, String address) {
        super(userId, firstName, lastName, username, password, active);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}
