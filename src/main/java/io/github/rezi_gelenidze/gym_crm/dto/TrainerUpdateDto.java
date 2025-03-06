package io.github.rezi_gelenidze.gym_crm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerUpdateDto {

    @NotBlank(message = "Specialization is required")
    private String specialization;
}
