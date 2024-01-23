package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "ingredientId")
@ToString(of = {"name", "allergic"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredient")
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id", nullable = false, unique = true)
    private Integer ingredientId;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "allergic")
    private Boolean allergic;

}
