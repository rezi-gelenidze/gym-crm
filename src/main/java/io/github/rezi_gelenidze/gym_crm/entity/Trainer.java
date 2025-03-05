package io.github.rezi_gelenidze.gym_crm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trainers")
public class Trainer extends User {
    private String specialization;

    public Trainer(Long userId, String firstName, String lastName, String username, String password, String specialization) {
        super(userId, firstName, lastName, username, password, true);
        this.specialization = specialization;
    }
}