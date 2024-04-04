package pl.Aevise.SupperSpeed.infrastructure.security.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "supperUserId")
@ToString(of = {"email", "active", "creationDateTime", "lastLoginDateTime"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supper_user")
public class SupperUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer supperUserId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "creation_date_time", nullable = false)
    private OffsetDateTime creationDateTime;

    @Column(name = "last_login_date_time", nullable = false)
    private OffsetDateTime lastLoginDateTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private RolesEntity role;
}
