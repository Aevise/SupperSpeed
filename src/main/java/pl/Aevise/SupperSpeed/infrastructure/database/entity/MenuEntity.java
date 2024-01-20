package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "menuId")
@ToString(of = {"name", "description"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "menu")
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id", nullable = false, unique = true)
    private Integer menuId;

    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @Column(name = "description", length = 128)
    private String description;

}
