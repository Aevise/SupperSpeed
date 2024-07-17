package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.controller.utils.OrderStatus;
import pl.Aevise.SupperSpeed.api.dto.DishListDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.SupperOrderDTO;
import pl.Aevise.SupperSpeed.api.dto.TotalRestaurantRatingDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.SupperOrderMapper;
import pl.Aevise.SupperSpeed.business.dao.SupperOrderDAO;
import pl.Aevise.SupperSpeed.domain.SupperOrder;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.StatusListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.ClientEntityMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@Slf4j
@Service
@AllArgsConstructor
public class SupperOrderService {
    private final SupperOrderDAO supperOrderDAO;
    private final SupperOrderMapper supperOrderMapper;

    private final ClientProfileService clientProfileService;
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
        SupperOrderEntity supperOrderEntity = buildSupperOrderEntity(restaurantId, clientEmail);
        SupperOrderEntity newOrder = supperOrderDAO.createNewOrder(supperOrderEntity);
        log.info("Successfully created order: [{}]", newOrder.getOrderId());
        return newOrder;
    }

    @Transactional
    public boolean updateOrderToPaid(Integer orderId) {
        SupperOrder fetchedOrder = getOrderById(orderId);
        boolean success = supperOrderDAO.updateOrderToPaid(fetchedOrder);
        if (success) {
            log.info("Successfully changed order: [{}] status to [{}]", orderId, OrderStatus.PAID.name().toUpperCase());
        } else {
            log.warn("Could not change order: [{}] status to [{}]", orderId, OrderStatus.PAID.name().toUpperCase());
        }
        return success;
    }

    @Transactional
    public void cancelOrder(Integer orderId) {
        SupperOrder fetchedOrder = getOrderById(orderId);

        SupperOrder updatedOrder = supperOrderDAO.cancelOrder(fetchedOrder);
        if (updatedOrder.getStatus().getStatusId() == 6) {
            log.info("Successfully cancelled order: [{}]", orderId);
        }
        log.warn("Could not cancel order: [{}]", orderId);
    }

    public void proceedOrder(Integer orderId) {
        SupperOrder fetchedOrder = supperOrderDAO.findById(orderId);
        Integer orderStatus = fetchedOrder.getStatus().getStatusId();

        if (orderStatus > OrderStatus.NEW.getStatusId() &&
                orderStatus < OrderStatus.REALIZED.getStatusId()) {
            SupperOrder updatedOrder = supperOrderDAO.proceedOrder(orderStatus + 1, fetchedOrder);
            if (updatedOrder.getStatus().getStatusId() > orderStatus) {
                log.info("Successfully proceeded order: [{}]", orderId);
            } else {
                log.warn("Could not proceed order: [{}]", orderId);
            }
        }

    }

    private SupperOrder getOrderById(Integer orderId) {
        SupperOrder order = supperOrderDAO.findById(orderId);
        if (order != null) {
            log.info("Successfully fetched order with id: [{}]", orderId);
            return order;
        }
        log.warn("Could not fetch order with id: [{}]", orderId);
        return null;
    }

    private SupperOrderEntity buildSupperOrderEntity(Integer restaurantId, String clientEmail) {
        return SupperOrderEntity.builder()
                .client(
                        clientEntityMapper
                                .mapToEntity(
                                        clientProfileService
                                                .findClientByEmail(clientEmail).get()))
                .restaurant(RestaurantEntity.builder()
                        .id(restaurantId)
                        .build())
                .status(StatusListEntity.builder()
                        .statusId(1)
                        .build())
                .orderDateTime(OffsetDateTime.now())
                .build();
    }

    public BigDecimal extractTotalOrderValue(List<DishListDTO> dishes) {
        BigDecimal value = BigDecimal.ZERO;
        for (DishListDTO dish : dishes) {
            value = value
                    .add(dish.getDishDTO().getPrice()
                            .multiply(BigDecimal.valueOf(dish.getQuantity())))
                    .setScale(2, RoundingMode.HALF_UP);
        }
        return value;
    }

    public void updateOrder(UserRatingEntity userRating, Integer orderId) {
        Optional<SupperOrderEntity> order = supperOrderDAO.getOrderById(orderId);
        if (order.isPresent()) {
            log.info("Found order with id: [{}]", order);
            SupperOrderEntity supperOrderEntity = order.get();
            supperOrderEntity.setUserRating(userRating);
            supperOrderDAO.saveOrder(supperOrderEntity);
        }
    }

    public TreeMap<Integer, List<Double>> getRestaurantsRatingBasedOnOrders(List<RestaurantDTO> restaurants) {
        TreeMap<Integer, List<Double>> restaurantsRating = new TreeMap<>();
        double noRating = 0.0;

        if (!restaurants.isEmpty()) {
            for (RestaurantDTO restaurant : restaurants) {
                if (restaurant.getIsShown()) {
                    Integer restaurantId = restaurant.getRestaurantId();
                    List<SupperOrderDTO> ratedOrdersForRestaurant = getRatedOrdersByRestaurantId(restaurantId);

                    if (!ratedOrdersForRestaurant.isEmpty()) {
                        log.info("Found [{}] orders for restaurant with id [{}]", ratedOrdersForRestaurant.size(), restaurantId);
                        restaurantsRating.putIfAbsent(restaurantId, calculateRestaurantRatingBasedOnRatedOrders(ratedOrdersForRestaurant));
                    } else {
                        log.info("Restaurant with id [{}] not rated yet", restaurantId);
                        restaurantsRating.putIfAbsent(restaurantId, List.of(noRating, noRating, noRating));
                    }
                }
            }
        }
        return restaurantsRating;
    }

    public List<Double> calculateRestaurantRatingBasedOnRatedOrders(List<SupperOrderDTO> orders) {
        /***
         * returns a list with food and delivery rating
         * List<(foodRating), (deliveryRating), (ratedOrders)>
         ***/
        List<Double> rating = new ArrayList<>();

        if (!orders.isEmpty()) {
            double amountOfOrders = orders.size();
            double summedFoodRating = 0;
            double summedDeliveryRating = 0;


            for (SupperOrderDTO order : orders) {
                summedFoodRating += order.getUserRatingDTO().getFoodRating();
                summedDeliveryRating += order.getUserRatingDTO().getDeliveryRating();
            }
            rating.add(summedFoodRating / amountOfOrders);
            rating.add(summedDeliveryRating / amountOfOrders);
            rating.add(amountOfOrders);
        }

        return rating;
    }

    public List<SupperOrderDTO> getRatedOrdersByRestaurantId(Integer restaurantId) {
        List<SupperOrder> ratedOrdersByRestaurantId = supperOrderDAO.getRatedOrdersByRestaurantId(restaurantId);
        if (!ratedOrdersByRestaurantId.isEmpty()) {
            log.info("Found [{}] rated orders for restaurant with id: [{}]", ratedOrdersByRestaurantId.size(), restaurantId);
            return ratedOrdersByRestaurantId.stream()
                    .map(supperOrderMapper::mapToDTO)
                    .toList();
        }
        log.info("Could not find rated orders for restaurant with id: [{}]", restaurantId);
        return List.of();
    }

    public Page<SupperOrderDTO> getRatedOrdersByRestaurantId(Integer restaurantId, PageRequest pageRequest) {
        Page<SupperOrder> ratedOrdersByRestaurantId = supperOrderDAO.getRatedOrdersByRestaurantId(restaurantId, pageRequest);
        if (!ratedOrdersByRestaurantId.isEmpty()) {
            log.info("Found [{}]/[{}], page [{}]/[{}] rated orders for restaurant with id: [{}]",
                    ratedOrdersByRestaurantId.getNumberOfElements(),
                    ratedOrdersByRestaurantId.getTotalElements(),
                    ratedOrdersByRestaurantId.getNumber(),
                    ratedOrdersByRestaurantId.getTotalPages(),
                    restaurantId);
            return ratedOrdersByRestaurantId
                    .map(supperOrderMapper::mapToDTO);
        }
        log.info("Could not find rated orders for restaurant with id: [{}]", restaurantId);
        return Page.empty();
    }

    public Page<SupperOrderDTO> getRatedOrdersByRestaurantName(String restaurantName, PageRequest pageRequest) {
        Page<SupperOrder> ratedOrdersByRestaurantName = supperOrderDAO.getRatedOrdersByRestaurantName(restaurantName, pageRequest);
        if (!ratedOrdersByRestaurantName.isEmpty()) {
            log.info("Found [{}]/[{}], page [{}]/[{}] rated orders for restaurant with name: [{}]",
                    ratedOrdersByRestaurantName.getNumberOfElements(),
                    ratedOrdersByRestaurantName.getTotalElements(),
                    ratedOrdersByRestaurantName.getNumber(),
                    ratedOrdersByRestaurantName.getTotalPages(),
                    restaurantName);
            return ratedOrdersByRestaurantName
                    .map(supperOrderMapper::mapToDTO);
        }
        log.info("Could not find rated orders for restaurant with name: [{}]", restaurantName);
        return Page.empty();
    }

    public List<SupperOrderDTO> getRatedOrdersByRestaurantName(String restaurantName) {
        List<SupperOrder> ratedOrdersByRestaurantName = supperOrderDAO.getRatedOrdersByRestaurantName(restaurantName);
        if (!ratedOrdersByRestaurantName.isEmpty()) {
            log.info("Found [{}] rated orders for restaurant with name: [{}]", ratedOrdersByRestaurantName.size(), restaurantName);
            return ratedOrdersByRestaurantName.stream()
                    .map(supperOrderMapper::mapToDTO)
                    .toList();
        }
        log.info("Could not find rated orders for restaurant with name: [{}]", restaurantName);
        return List.of();
    }

    public TotalRestaurantRatingDTO getRestaurantRating(List<SupperOrderDTO> ratedOrders) {
        List<Double> ratings = calculateRestaurantRatingBasedOnRatedOrders(ratedOrders);
        return TotalRestaurantRatingDTO.builder()
                .amountOfRatedOrders(ratedOrders.size())
                .restaurantId(ratedOrders.get(0).getRestaurantDTO().getRestaurantId())
                .deliveryRating(ratings.get(1))
                .foodRating(ratings.get(0))
                .build();
    }

}
