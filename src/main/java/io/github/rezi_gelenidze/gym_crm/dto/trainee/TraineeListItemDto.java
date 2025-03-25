package io.github.rezi_gelenidze.gym_crm.dto.trainee;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeListItemDto {
    private String username;

    private String firstName;

    private String lastName;
}
