package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.RestaurantDAO;
import pl.Aevise.SupperSpeed.domain.Image;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ImageEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.ImageEntityMapper;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.RestaurantEntityMapper;
import pl.Aevise.SupperSpeed.infrastructure.security.database.jpa.SupperUserJpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class RestaurantRepository implements RestaurantDAO {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;

    private final ImageEntityMapper imageEntityMapper;

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
    public Optional<Restaurant> findByUserEmail(String email) {

        Optional<RestaurantEntity> restaurantEntity = restaurantJpaRepository.findBySupperUser_Email(email);
        return restaurantEntity.map(restaurantEntityMapper::mapFromEntity);

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
    public void updateRestaurantLogo(Integer userId, Image image) {
        Optional<RestaurantEntity> restaurantEntity = restaurantJpaRepository.findById(userId);
        ImageEntity imageEntity = imageEntityMapper.mapToEntity(image);

        if (restaurantEntity.isPresent()) {
            RestaurantEntity restaurant = restaurantEntity.get();
            restaurant.setImageEntity(imageEntity);
            restaurantJpaRepository.saveAndFlush(restaurant);
        }
    }

    @Override
    public void toggleRestaurantVisibility(Integer userId) {
        Optional<RestaurantEntity> restaurantEntity = restaurantJpaRepository.findById(userId);
        if (restaurantEntity.isPresent()) {
            RestaurantEntity restaurant = restaurantEntity.get();
            boolean negation = !restaurant.getIsShown();
            restaurant.setIsShown(negation);
            restaurantJpaRepository.saveAndFlush(restaurant);
        } else {
            throw new EntityNotFoundException("Restaurant not found");
        }
    }

    @Override
    public Restaurant detachUserFromRestaurant(String email) {
        Optional<RestaurantEntity> bySupperUserEmail = restaurantJpaRepository.findBySupperUser_Email(email);

        if (bySupperUserEmail.isPresent()) {
            RestaurantEntity restaurant = bySupperUserEmail.get();
            restaurant.setIsShown(false);
            restaurant.setSupperUser(null);
            restaurantJpaRepository.saveAndFlush(restaurant);
            return restaurantEntityMapper.mapFromEntity(restaurant);
        } else {
            throw new EntityNotFoundException("Could not find restaurant");
        }
    }

    @Override
    public List<String> getDistinctCitiesWithRestaurants() {
        return restaurantJpaRepository.findDistinctCitiesForRestaurants();
    }

    @Override
    public Optional<AddressEntity> getAddressByRestaurantId(Integer restaurantId) {
        return restaurantJpaRepository.findAddressByRestaurantId(restaurantId);
    }

}
