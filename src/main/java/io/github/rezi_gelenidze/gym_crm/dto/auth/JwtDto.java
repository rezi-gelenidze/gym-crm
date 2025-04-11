package io.github.rezi_gelenidze.gym_crm.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtDto {
    private String token;
}
