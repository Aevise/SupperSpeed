package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.RestaurantDAO;
import pl.Aevise.SupperSpeed.domain.Logo;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.LogoEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.LogoEntityMapper;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.jpa.SupperUserJpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class RestaurantRepository implements RestaurantDAO {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;

    private final LogoEntityMapper logoEntityMapper;

    private final SupperUserJpaRepository supperUserJpaRepository;

    private final EntityManager entityManager;

    @Override
    public Optional<Restaurant> findByName(String restaurantName) {
        return Optional.empty();
    }

    @Override
    public List<Restaurant> findAvailable() {
        return restaurantJpaRepository.findAll()
                .stream()
                .map(restaurantEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public void deleteRestaurantById(Integer id) {
        restaurantJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Restaurant> findByEmail(String email) {
        Optional<SupperUserEntity> userEntity = supperUserJpaRepository.findByEmail(email);

        return userEntity.flatMap(supperUserEntity ->
                restaurantJpaRepository
                        .findById(supperUserEntity.getSupperUserId())
                        .map(restaurantEntityMapper::mapFromEntity));
    }

    @Override
    public void updateRestaurantInformation(Restaurant restaurant, Integer userId) {
        Optional<RestaurantEntity> currentRestaurantData = restaurantJpaRepository.findById(userId);

        if (currentRestaurantData.isPresent()) {
            RestaurantEntity restaurantEntity = currentRestaurantData.get();

            restaurantEntity.setRestaurantName(restaurant.getRestaurantName());
            restaurantEntity.setOpenHour(restaurant.getOpenHour());
            restaurantEntity.setCloseHour(restaurant.getCloseHour());
            restaurantEntity.setPhone(restaurant.getPhone());
            restaurantJpaRepository.saveAndFlush(restaurantEntity);
        }
    }

    @Override
    public Optional<Restaurant> findById(Integer id) {
        return restaurantJpaRepository
                .findById(id)
                .map(restaurantEntityMapper::mapFromEntity);
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantJpaRepository
                .findAll()
                .stream()
                .map(restaurantEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public List<Restaurant> findAllByCity(String city) {
        return restaurantJpaRepository
                .findAllByAddress_City(city)
                .stream()
                .map(restaurantEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public RestaurantEntity createRestaurant(RestaurantEntity restaurantEntity) {
        return restaurantJpaRepository.saveAndFlush(restaurantEntity);
    }

    @Override
    public void updateRestaurantLogo(Integer userId, Logo logo) {
        Optional<RestaurantEntity> restaurantEntity = restaurantJpaRepository.findById(userId);
        LogoEntity logoEntity = logoEntityMapper.mapToEntity(logo);

        if (restaurantEntity.isPresent()) {
            RestaurantEntity restaurant = restaurantEntity.get();
            restaurant.setLogo(logoEntity);
            restaurantJpaRepository.saveAndFlush(restaurant);
        }
    }
}
