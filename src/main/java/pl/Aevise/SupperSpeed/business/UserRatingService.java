package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.UserRatingDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.UserRatingMapper;
import pl.Aevise.SupperSpeed.business.dao.UserRatingDAO;
import pl.Aevise.SupperSpeed.domain.UserRating;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.UserRatingEntityMapper;

import java.time.OffsetDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class UserRatingService {

    private final UserRatingDAO userRatingDAO;
    private final UserRatingMapper userRatingMapper;
    private final UserRatingEntityMapper userRatingEntityMapper;

    private final SupperOrderService supperOrderService;

    @Transactional
    public void saveNewComment(UserRatingDTO userRatingDTO, Integer orderId) {
        userRatingDTO.setRatingDateTime(OffsetDateTime.now());
        UserRating userRating = userRatingMapper.mapFromDTO(userRatingDTO);

        UserRatingEntity userRatingEntity = userRatingDAO.saveNewUserRating(userRating);

        supperOrderService.updateOrder(userRatingEntity, orderId);
    }

}
