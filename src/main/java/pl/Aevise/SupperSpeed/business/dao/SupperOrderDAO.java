package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.SupperOrder;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;

import java.util.List;

public interface SupperOrderDAO {
    List<SupperOrder> getOrdersByRestaurantId(Integer restaurantId);

    List<SupperOrder> getOrdersByClientId(Integer clientId);

    void createNewOrder(SupperOrderEntity supperOrderEntity);
}
