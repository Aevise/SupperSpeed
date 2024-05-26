package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.*;
import pl.Aevise.SupperSpeed.api.dto.mapper.OffsetDateTimeMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.UserRatingMapper;
import pl.Aevise.SupperSpeed.business.dao.UserRatingDAO;
import pl.Aevise.SupperSpeed.domain.SupperOrder;
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

    private OffsetDateTimeMapper offsetDateTimeMapper;

    private final DishListService dishListService;

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

    public TotalRestaurantRatingDTO getRestaurantRating(Integer restaurantId){
        List<SupperOrderDTO> ratedOrdersByRestaurantId = supperOrderService.getRatedOrdersByRestaurantId(restaurantId);
        if(!ratedOrdersByRestaurantId.isEmpty()){
            log.info("Successfully got rating for restaurant with id: [{}]", restaurantId);
            return supperOrderService.getRestaurantRating(ratedOrdersByRestaurantId);
        }
        log.warn("Could not get total rating for restaurant with id: [{}]", restaurantId);
        return null;
    }

    public List<OpinionDTO> getOpinionsAboutOrdersFromRestaurant(Integer restaurantId) {
        List<SupperOrderDTO> ratedOrdersByRestaurantId = supperOrderService.getRatedOrdersByRestaurantId(restaurantId);
        List<OpinionDTO> opinionsAboutRestaurant = new ArrayList<>();

        if(!ratedOrdersByRestaurantId.isEmpty()){
            log.info("Found [{}] rated orders for restaurant with id: [{}]", ratedOrdersByRestaurantId.size(), restaurantId);
            for (SupperOrderDTO order : ratedOrdersByRestaurantId) {
                List<DishListDTO> dishesByOrderId = dishListService.getDishesByOrderId(order.getOrderId());
                HashMap<DishDTO, Integer> dishes = new HashMap<>();
                if(!dishesByOrderId.isEmpty()){
                    for (DishListDTO dishListDTO : dishesByOrderId) {
                        dishes.putIfAbsent(dishListDTO.getDishDTO(), dishListDTO.getQuantity());
                    }
                }
                opinionsAboutRestaurant.add(buildOpinionDTO(order, order.getUserRatingDTO(), dishes));
            }
            opinionsAboutRestaurant.sort(Comparator.comparing(OpinionDTO::getOrderId));
        }else {
            log.info("No rated orders found for restaurant with id [{}]", restaurantId);
        }
        return opinionsAboutRestaurant;
    }

    private OpinionDTO buildOpinionDTO(
            SupperOrderDTO supperOrderDTO,
            UserRatingDTO userRatingDTO,
            Map<DishDTO, Integer> dishes
            ){
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
