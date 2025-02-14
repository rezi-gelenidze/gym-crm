package io.github.rezi_gelenidze.gym_crm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Vertical partitioned entity of User
@Getter
@Setter
@AllArgsConstructor
public class Trainee extends User {
    private User user;
    private String dateOfBirth;
    private String address;
}
