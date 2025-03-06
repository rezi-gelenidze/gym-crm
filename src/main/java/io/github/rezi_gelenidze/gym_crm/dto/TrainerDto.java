package io.github.rezi_gelenidze.gym_crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerDto {
    private String firstName;
    private String lastName;
    private String specialization;
}
