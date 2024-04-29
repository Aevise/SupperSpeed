package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.UserRatingDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.OffsetDateTimeMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.UserRatingMapper;
import pl.Aevise.SupperSpeed.business.dao.UserRatingDAO;
import pl.Aevise.SupperSpeed.domain.UserRating;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantResponseEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.UserRatingEntityMapper;

import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserRatingService {

    private final UserRatingDAO userRatingDAO;
    private final UserRatingMapper userRatingMapper;
    private final UserRatingEntityMapper userRatingEntityMapper;

    private OffsetDateTimeMapper offsetDateTimeMapper;

    private final SupperOrderService supperOrderService;

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

    public UserRating getUserRatingById(Integer userRatingId){
        Optional<UserRatingEntity> userRatingById = userRatingDAO.getUserRatingById(userRatingId);
        if (userRatingById.isPresent()){
            log.info("Successfully retrieved userRating with id: [{}]", userRatingById);
            return userRatingEntityMapper.mapFromEntity(userRatingById.get());
        }
        log.warn("Could not find user rating with id: [{}]", userRatingById);
        return null;
    }
}
