package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.security.RolesEntity;

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
    @JoinColumn(name = "dish_id", referencedColumnName = "dish_id")
    private DishEntity dish;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private SupperOrderEntity order;

    @Column(name = "quantity")
    private String quantity;

}
