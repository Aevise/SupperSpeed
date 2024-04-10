package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.controller.utils.OrderStatus;
import pl.Aevise.SupperSpeed.api.dto.DishListDTO;
import pl.Aevise.SupperSpeed.business.dao.SupperOrderDAO;
import pl.Aevise.SupperSpeed.domain.SupperOrder;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.StatusListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.SupperOrderRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.ClientEntityMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SupperOrderService {
    private final SupperOrderDAO supperOrderDAO;

    private final ClientService clientService;
    private final UserService userService;
    private final ClientEntityMapper clientEntityMapper;

    @Transactional
    public List<SupperOrder> getOrdersByRestaurantId(Integer restaurantId) {
        List<SupperOrder> ordersByRestaurantId = supperOrderDAO.getOrdersByRestaurantId(restaurantId);
        log.info("Found: [{}] orders", ordersByRestaurantId.size());
        return ordersByRestaurantId;
    }

    @Transactional
    public List<SupperOrder> getOrdersByClientId(Integer clientId) {
        List<SupperOrder> ordersByClientId = supperOrderDAO.getOrdersByClientId(clientId);
        log.info("Found: [{}] orders", ordersByClientId.size());
        return ordersByClientId;
    }

    //TODO zmienić na zwykłe supperOrder a mapowanie dopiero zrobić w SupperOrderRepository
    @Transactional
    public SupperOrderEntity createNewOrder(Integer restaurantId, String clientEmail) {
        SupperOrderEntity newOrder = supperOrderDAO.createNewOrder(buildSupperOrderEntity(restaurantId, clientEmail));
        log.info("Successfully created order: [{}]", newOrder.getOrderId());
        return newOrder;
    }

    @Transactional
    public boolean updateOrderToPaid(Integer orderId) {
        SupperOrder fetchedOrder = getOrderById(orderId);
        boolean success = supperOrderDAO.updateOrderToPaid(fetchedOrder);
        if(success){
            log.info("Successfully changed order: [{}] status to [{}]", orderId, OrderStatus.PAID.name().toUpperCase());
        }else {
            log.warn("Could not change order: [{}] status to [{}]", orderId, OrderStatus.PAID.name().toUpperCase());
        }
        return success;

    }

    private SupperOrder getOrderById(Integer orderId) {
        SupperOrder order = supperOrderDAO.findById(orderId);
        if (order != null){
            log.info("Successfully fetched order with id: [{}]", orderId);
            return order;
        }
        log.warn("Could not fetch order with id: [{}]", orderId);
        return null;
    }

    private SupperOrderEntity buildSupperOrderEntity(Integer restaurantId, String clientEmail) {
        return SupperOrderEntity.builder()
                .client(clientEntityMapper
                        .mapToEntity(
                                clientService
                                        .findById(
                                                userService
                                                        .findUserByEmail(clientEmail)
                                                        .get()
                                                        .getSupperUserId())
                                        .get()))
                .restaurant(RestaurantEntity.builder()
                        .id(restaurantId)
                        .build())
                .status(StatusListEntity.builder()
                        .statusId(1)
                        .build())
                .orderDateTime(OffsetDateTime.now())
                .build();
    }

    public BigDecimal extractTotalOrderValue(List<DishListDTO> dishes){
        BigDecimal value = BigDecimal.ZERO;
        for (DishListDTO dish : dishes) {
            value = value
                    .add(dish.getDishDTO().getPrice()
                            .multiply(BigDecimal.valueOf(dish.getQuantity())))
                    .setScale(2, RoundingMode.HALF_UP);
        }
        return value;
    }
}
