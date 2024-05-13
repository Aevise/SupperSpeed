package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "supperUser")
@ToString(of = {"name", "surname"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class ClientEntity {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", unique = true, nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SupperUserEntity supperUser;

    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @Column(name = "surname", length = 32)
    private String surname;

    @Column(name = "phone")
    private String phone;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", unique = true, nullable = false)
    private AddressEntity address;

    //TODO czy tutaj dodac kiedys Cascade? Do sprawdzenia na testach
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private Set<SupperOrderEntity> orders;

}
