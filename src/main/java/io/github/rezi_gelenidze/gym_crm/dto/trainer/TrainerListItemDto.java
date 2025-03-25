package io.github.rezi_gelenidze.gym_crm.dto.trainer;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerListItemDto {
    private String username;

    private String firstName;

    private String lastName;

    private Long specializationId;
}
