package io.github.rezi_gelenidze.gym_crm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Vertical partitioned entity of User
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trainer {
    private User user;
    private String specialization;
}
