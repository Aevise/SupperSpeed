package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.api.controller.utils.OrderStatus;
import pl.Aevise.SupperSpeed.business.dao.SupperOrderDAO;
import pl.Aevise.SupperSpeed.domain.SupperOrder;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.StatusListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.SupperOrderJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.SupperOrderEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SupperOrderRepository implements SupperOrderDAO {
    private final SupperOrderJpaRepository supperOrderJpaRepository;
    private final SupperOrderEntityMapper supperOrderEntityMapper;


    @Override
    public List<SupperOrder> getOrdersByRestaurantId(Integer restaurantId) {
        List<SupperOrderEntity> allOrdersFromRestaurant = supperOrderJpaRepository.findAllByRestaurant_Id(restaurantId);

        if (!allOrdersFromRestaurant.isEmpty()) {
            return allOrdersFromRestaurant
                    .stream()
                    .map(supperOrderEntityMapper::mapFromEntity)
                    .toList();
        }
        return List.of();
    }

    @Override
    public List<SupperOrder> getOrdersByClientId(Integer clientId) {
        List<SupperOrderEntity> allByClientId = supperOrderJpaRepository.findAllByClient_Id(clientId);

        if (!allByClientId.isEmpty()) {
            return allByClientId
                    .stream()
                    .map(supperOrderEntityMapper::mapFromEntity)
                    .toList();
        }
        return List.of();
    }

    @Override
    public SupperOrderEntity createNewOrder(SupperOrderEntity supperOrderEntity) {
        return supperOrderJpaRepository.saveAndFlush(supperOrderEntity);
    }

    @Override
    public boolean updateOrderToPaid(SupperOrder order) {
        if (order.getStatus().getDescription().equalsIgnoreCase(OrderStatus.NEW.name())) {
            SupperOrderEntity supperOrderEntity = supperOrderEntityMapper.mapToEntity(order);
            supperOrderEntity
                    .setStatus(StatusListEntity.builder()
                            .statusId(2)
                            .build());
            supperOrderJpaRepository.saveAndFlush(supperOrderEntity);
            return true;
        }
        return false;
    }

    @Override
    public SupperOrder findById(Integer orderId) {
        Optional<SupperOrderEntity> order = supperOrderJpaRepository.findById(orderId);
        return order.map(supperOrderEntityMapper::mapFromEntity).orElse(null);
    }
}
