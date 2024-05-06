package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.UserRatingDAO;
import pl.Aevise.SupperSpeed.domain.UserRating;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.UserRatingJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.UserRatingEntityMapper;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRatingRepository implements UserRatingDAO {

    private final UserRatingJpaRepository userRatingJpaRepository;
    private final UserRatingEntityMapper userRatingEntityMapper;

    @Override
    public UserRatingEntity saveNewUserRating(UserRating userRating) {
        return userRatingJpaRepository.saveAndFlush(userRatingEntityMapper.mapToEntity(userRating));
    }

    @Override
    public void saveUserRating(UserRatingEntity userRatingEntity) {
        userRatingJpaRepository.saveAndFlush(userRatingEntity);
    }

    @Override
    public Optional<UserRatingEntity> getUserRatingById(Integer userRatingId) {
        return userRatingJpaRepository.findById(userRatingId);
    }
}
