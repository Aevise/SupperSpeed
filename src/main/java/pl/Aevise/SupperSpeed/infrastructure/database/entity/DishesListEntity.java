package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"dish", "order"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dishes_list")
public class DishesListEntity implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId
    private DishEntity dish;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId
    private SupperOrderEntity order;

    @Column(name = "quantity")
    private String quantity;

}
