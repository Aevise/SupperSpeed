package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "cuisine")
@ToString(of = {"cuisine"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cuisine")
public class CuisineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuisine_id", nullable = false, unique = true)
    private Integer cuisineId;

    @Column(name = "cuisine_name", length = 64, nullable = false)
    private String cuisine;

}
