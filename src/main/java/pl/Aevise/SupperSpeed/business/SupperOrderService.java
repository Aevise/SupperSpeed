package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.business.dao.SupperOrderDAO;
import pl.Aevise.SupperSpeed.domain.SupperOrder;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.StatusListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.ClientEntityMapper;

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

    @Transactional
    public SupperOrderEntity createNewOrder(Integer restaurantId, String clientEmail) {
        SupperOrderEntity newOrder = supperOrderDAO.createNewOrder(buildSupperOrderEntity(restaurantId, clientEmail));
        log.info("Successfully created order: [{}]", newOrder.getOrderId());
        return newOrder;
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
}
