package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.SupperOrder;

import java.util.List;

public interface SupperOrderDAO {
    List<SupperOrder> getOrdersByRestaurantId(Integer restaurantId);

    List<SupperOrder> getOrdersByClientId(Integer clientId);
}
