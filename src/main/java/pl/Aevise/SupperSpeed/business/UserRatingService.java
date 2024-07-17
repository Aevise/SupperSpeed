package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.*;
import pl.Aevise.SupperSpeed.api.dto.mapper.OffsetDateTimeMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.OpinionMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.UserRatingMapper;
import pl.Aevise.SupperSpeed.business.dao.UserRatingDAO;
import pl.Aevise.SupperSpeed.domain.UserRating;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantResponseEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.UserRatingEntityMapper;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserRatingService {

    private final UserRatingDAO userRatingDAO;
    private final UserRatingMapper userRatingMapper;
    private final UserRatingEntityMapper userRatingEntityMapper;

    private final SupperOrderService supperOrderService;
    private final DishListService dishListService;
    private final OpinionMapper opinionMapper;
    private OffsetDateTimeMapper offsetDateTimeMapper;

    @Transactional
    public void saveNewComment(UserRatingDTO userRatingDTO, Integer orderId) {
        userRatingDTO.setRatingDateTime(offsetDateTimeMapper.mapOffsetDateTimeToString(OffsetDateTime.now()));
        UserRating userRating = userRatingMapper.mapFromDTO(userRatingDTO);

        UserRatingEntity userRatingEntity = userRatingDAO.saveNewUserRating(userRating);

        supperOrderService.updateOrder(userRatingEntity, orderId);
    }

    @Transactional
    public void updateUserRating(RestaurantResponseEntity restaurantResponseEntity, Integer userRatingId) {
        UserRating userRatingById = getUserRatingById(userRatingId);

        UserRatingEntity userRatingEntity = userRatingEntityMapper.mapToEntity(userRatingById);
        userRatingEntity.setRestaurantResponseEntity(restaurantResponseEntity);

        userRatingDAO.saveUserRating(userRatingEntity);
    }

    public UserRating getUserRatingById(Integer userRatingId) {
        Optional<UserRatingEntity> userRatingById = userRatingDAO.getUserRatingById(userRatingId);
        if (userRatingById.isPresent()) {
            log.info("Successfully retrieved userRating with id: [{}]", userRatingById);
            return userRatingEntityMapper.mapFromEntity(userRatingById.get());
        }
        log.warn("Could not find user rating with id: [{}]", userRatingById);
        return null;
    }

    public TotalRestaurantRatingDTO getRestaurantRating(Integer restaurantId) {
        List<SupperOrderDTO> ratedOrdersByRestaurantId = supperOrderService.getRatedOrdersByRestaurantId(restaurantId);
        if (!ratedOrdersByRestaurantId.isEmpty()) {
            log.info("Successfully got rating for restaurant with id: [{}]", restaurantId);
            return supperOrderService.getRestaurantRating(ratedOrdersByRestaurantId);
        }
        log.warn("Could not get total rating for restaurant with id: [{}]", restaurantId);
        return null;
    }

    public TotalRestaurantRatingDTO getRestaurantRating(String restaurantName) {
        List<SupperOrderDTO> ratedOrdersByRestaurantName = supperOrderService.getRatedOrdersByRestaurantName(restaurantName);
        if (!ratedOrdersByRestaurantName.isEmpty()) {
            log.info("Successfully got rating for restaurant with name: [{}]", restaurantName);
            return supperOrderService.getRestaurantRating(ratedOrdersByRestaurantName);
        }
        log.warn("Could not get total rating for restaurant with name: [{}]", restaurantName);
        return null;
    }

    public Page<OpinionDTO> getOpinionsAboutOrdersFromRestaurant(Integer restaurantId, PageRequest pageRequest) {
        Page<SupperOrderDTO> ratedOrdersByRestaurantId = supperOrderService.getRatedOrdersByRestaurantId(restaurantId, pageRequest);
        List<OpinionDTO> opinionsAboutRestaurant = new ArrayList<>();

        if (!ratedOrdersByRestaurantId.isEmpty()) {
            log.info("Fetched [{}]/[{}] rated orders for restaurant with id: [{}]",
                    ratedOrdersByRestaurantId.getNumberOfElements(),
                    ratedOrdersByRestaurantId.getTotalElements(),
                    restaurantId);
            for (SupperOrderDTO order : ratedOrdersByRestaurantId) {
                List<DishListDTO> dishesByOrderId = dishListService.getDishesByOrderId(order.getOrderId());
                HashMap<DishDTO, Integer> dishes = new HashMap<>();
                if (!dishesByOrderId.isEmpty()) {
                    for (DishListDTO dishListDTO : dishesByOrderId) {
                        dishes.putIfAbsent(dishListDTO.getDishDTO(), dishListDTO.getQuantity());
                    }
                }
                opinionsAboutRestaurant.add(buildOpinionDTO(order, order.getUserRatingDTO(), dishes));
            }
        } else {
            log.info("No rated orders found for restaurant with id [{}]", restaurantId);
        }
        if (opinionsAboutRestaurant.isEmpty()) {
            return Page.empty();
        }
        return new PageImpl<>(opinionsAboutRestaurant,
                pageRequest,
                opinionsAboutRestaurant.size()
        );
    }

    public Page<OpinionDTO> getOpinionsAboutOrdersFromRestaurant(String restaurantName, PageRequest pageRequest) {
        Page<SupperOrderDTO> ratedOrdersByRestaurantId = supperOrderService.getRatedOrdersByRestaurantName(restaurantName, pageRequest);
        List<OpinionDTO> opinionsAboutRestaurant = new ArrayList<>();

        if (!ratedOrdersByRestaurantId.isEmpty()) {
            log.info("Fetched [{}]/[{}] rated orders for restaurant with name: [{}]",
                    ratedOrdersByRestaurantId.getNumberOfElements(),
                    ratedOrdersByRestaurantId.getTotalElements(),
                    restaurantName);
            for (SupperOrderDTO order : ratedOrdersByRestaurantId) {
                List<DishListDTO> dishesByOrderId = dishListService.getDishesByOrderId(order.getOrderId());
                HashMap<DishDTO, Integer> dishes = new HashMap<>();
                if (!dishesByOrderId.isEmpty()) {
                    for (DishListDTO dishListDTO : dishesByOrderId) {
                        dishes.putIfAbsent(dishListDTO.getDishDTO(), dishListDTO.getQuantity());
                    }
                }
                opinionsAboutRestaurant.add(buildOpinionDTO(order, order.getUserRatingDTO(), dishes));
            }
        } else {
            log.info("No rated orders found for restaurant with name [{}]", restaurantName);
        }
        if (opinionsAboutRestaurant.isEmpty()) {
            return Page.empty();
        }
        return new PageImpl<>(opinionsAboutRestaurant,
                pageRequest,
                opinionsAboutRestaurant.size()
        );
    }

    public Page<RestOpinionDTO> getOpinionsAboutOrdersFromRestaurantForRest(String restaurantName, PageRequest pageRequest) {
        Page<OpinionDTO> opinionsPage = getOpinionsAboutOrdersFromRestaurant(restaurantName, pageRequest);

        List<RestOpinionDTO> restOpinions = opinionsPage.getContent()
                .stream()
                .map(opinionMapper::mapToRestDTO)
                .toList();

        return new PageImpl<>(restOpinions, pageRequest, opinionsPage.getTotalElements());

    }

    private OpinionDTO buildOpinionDTO(
            SupperOrderDTO supperOrderDTO,
            UserRatingDTO userRatingDTO,
            Map<DishDTO, Integer> dishes
    ) {
        return OpinionDTO.builder()
                .orderId(supperOrderDTO.getOrderId())
                .orderDateTime(supperOrderDTO.getOrderDateTime())
                .restaurantDTO(supperOrderDTO.getRestaurantDTO())
                .clientDTO(supperOrderDTO.getClientDTO())
                .userRatingDTO(userRatingDTO)
                .dishesAndQuantity(dishes)
                .build();
    }
}
