package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.UserRating;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;

public interface UserRatingDAO {
    UserRatingEntity saveNewUserRating(UserRating userRating);
}
