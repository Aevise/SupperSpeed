package pl.Aevise.SupperSpeed.infrastructure.security;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "roleId")
@ToString(of = "role_name")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false, unique = true)
    private Integer roleId;

    @Column(name = "role_name", length = 32, nullable = false)
    private String roleName;
}
