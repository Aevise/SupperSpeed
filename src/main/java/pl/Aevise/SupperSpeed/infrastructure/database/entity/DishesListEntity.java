package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DishesListKey;

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

    @EmbeddedId
    private DishesListKey id;

    @MapsId("dishId")
    @JoinColumn(name = "dish_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private DishEntity dish;

    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SupperOrderEntity order;

    @Column(name = "quantity")
    private String quantity;

}
