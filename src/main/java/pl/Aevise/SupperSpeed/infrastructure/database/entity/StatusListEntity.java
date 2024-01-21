package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "statusId")
@ToString(of = {"description"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "status_list")
public class StatusListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id", unique = true, nullable = false)
    private Integer statusId;

    @Column(name = "description", length = 32, nullable = false)
    private String description;
}
