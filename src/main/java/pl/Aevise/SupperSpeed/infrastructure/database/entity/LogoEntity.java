package pl.Aevise.SupperSpeed.infrastructure.database.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "logoId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logo")
public class LogoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logo_id", nullable = false, unique = true)
    private Integer logoId;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

}
