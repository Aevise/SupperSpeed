package pl.Aevise.SupperSpeed.infrastructure.security;

import jakarta.persistence.*;
import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.security.RolesEntity;

import java.time.OffsetDateTime;
import java.util.Set;

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
    @Column(name = "user_id")
    private Integer supperUserId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "creation_date_time")
    private OffsetDateTime creationDateTime;

    @Column(name = "last_login_date_time")
    private OffsetDateTime lastLoginDateTime;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RolesEntity> role;


}
