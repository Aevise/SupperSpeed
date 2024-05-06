package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.RestaurantResponseDAO;
import pl.Aevise.SupperSpeed.domain.RestaurantResponse;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantResponseEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.RestaurantResponseJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.RestaurantResponseEntityMapper;

@Repository
@AllArgsConstructor
public class RestaurantResponseRepository implements RestaurantResponseDAO {

    private final RestaurantResponseJpaRepository restaurantResponseJpaRepository;
    private final RestaurantResponseEntityMapper restaurantResponseEntityMapper;

    @Override
    public RestaurantResponseEntity saveRestaurantResponse(RestaurantResponse restaurantResponse) {
        RestaurantResponseEntity restaurantResponseEntity = restaurantResponseEntityMapper.mapToEntity(restaurantResponse);

        return restaurantResponseJpaRepository.saveAndFlush(restaurantResponseEntity);
    }
}
