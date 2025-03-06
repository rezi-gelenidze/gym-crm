package io.github.rezi_gelenidze.gym_crm.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeDto {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
