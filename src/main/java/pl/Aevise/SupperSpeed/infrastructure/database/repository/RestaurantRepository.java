package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.RestaurantDAO;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.RestaurantJpaRepository;
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
    private final SupperUserJpaRepository supperUserJpaRepository;

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
}
