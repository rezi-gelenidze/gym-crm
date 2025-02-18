package io.github.rezi_gelenidze.gym_crm.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trainee extends User {
    private String dateOfBirth;
    private String address;
}
