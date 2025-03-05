package io.github.rezi_gelenidze.gym_crm.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "training_types")
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long trainingTypeId;

    @Column(nullable = false, unique = true)
    String trainingTypeName;
}
