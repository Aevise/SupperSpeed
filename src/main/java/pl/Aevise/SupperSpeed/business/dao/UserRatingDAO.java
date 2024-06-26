package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.UserRating;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;

import java.util.Optional;

public interface UserRatingDAO {
    UserRatingEntity saveNewUserRating(UserRating userRating);

    void saveUserRating(UserRatingEntity userRatingEntity);

    Optional<UserRatingEntity> getUserRatingById(Integer userRatingId);
}
