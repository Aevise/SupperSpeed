package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.RestaurantResponseDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.OffsetDateTimeMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.RestaurantResponseMapper;
import pl.Aevise.SupperSpeed.business.dao.RestaurantResponseDAO;
import pl.Aevise.SupperSpeed.domain.RestaurantResponse;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantResponseEntity;

import java.time.OffsetDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class RestaurantResponseService {

    private final RestaurantResponseDAO restaurantResponseDAO;
    private final RestaurantResponseMapper restaurantResponseMapper;

    private final OffsetDateTimeMapper offsetDateTimeMapper;

    private final UserRatingService userRatingService;


    @Transactional
    public void saveRestaurantResponse(RestaurantResponseDTO restaurantResponseDTO, Integer userRatingId) {
        restaurantResponseDTO.setResponseDateTime(offsetDateTimeMapper.mapOffsetDateTimeToString(OffsetDateTime.now()));
        RestaurantResponse restaurantResponse = restaurantResponseMapper.mapFromDTO(restaurantResponseDTO);
        RestaurantResponseEntity restaurantResponseEntity = restaurantResponseDAO.saveRestaurantResponse(restaurantResponse);
        if (restaurantResponseEntity != null) {
            log.info("Successfully saved restaurant response with id: [{}]", restaurantResponseEntity.getRestaurantResponseId());
        }

        userRatingService.updateUserRating(restaurantResponseEntity, userRatingId);
    }

}
