package pl.Aevise.SupperSpeed.infrastructure.database.entity.utils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"dishId", "orderId"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DishesListKey implements Serializable {

    @Column(name = "dish_id")
    private Integer dishId;

    @Column(name = "order_id")
    private Integer orderId;

}
