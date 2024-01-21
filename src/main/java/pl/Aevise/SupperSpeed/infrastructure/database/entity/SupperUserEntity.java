package pl.Aevise.SupperSpeed.infrastructure.database.entity;

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
    @Column(name = "user_id")
    private Integer supperUserId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "phone")
    private String phone;

    @Column(name = "creation_date_time")
    private OffsetDateTime creationDateTime;

    @Column(name = "last_login_date_time")
    private OffsetDateTime lastLoginDateTime;


}
