package pl.Aevise.SupperSpeed.business.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.Aevise.SupperSpeed.domain.SupperOrder;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;

import java.util.List;
import java.util.Optional;

public interface SupperOrderDAO {
    List<SupperOrder> getOrdersByRestaurantId(Integer restaurantId);

    List<SupperOrder> getOrdersByClientId(Integer clientId);


    SupperOrderEntity createNewOrder(SupperOrderEntity supperOrderEntity);

    boolean updateOrderToPaid(SupperOrder order);

    SupperOrder findById(Integer orderId);

    SupperOrder cancelOrder(SupperOrder supperOrder);

    SupperOrder proceedOrder(int newStatus, SupperOrder fetchedOrder);

    Optional<SupperOrderEntity> getOrderById(Integer orderId);

    void saveOrder(SupperOrderEntity supperOrderEntity);

    Page<SupperOrder> getRatedOrdersByRestaurantId(Integer restaurantId, PageRequest pageRequest);
    Page<SupperOrder> getRatedOrdersByRestaurantName(String restaurantName, PageRequest pageRequest);
    List<SupperOrder> getRatedOrdersByRestaurantName(String restaurantName);

    List<SupperOrder> getRatedOrdersByRestaurantId(Integer restaurantId);
}
