package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.business.dao.RestaurantDAO;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.domain.SupperUser;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantDAO restaurantDAO;
    private final ProfileService profileService;

    @Transactional
    public void deleteRestaurantById(Integer id){
        restaurantDAO.deleteRestaurantById(id);
        log.info("Deleted restaurant with id: [{}]", id);
    }

    @Transactional
    public Optional<Restaurant> findRestaurantByEmail(String email){
        Optional<SupperUser> foundUser = profileService.findUserByEmail(email);

        if (foundUser.isPresent()){
            return restaurantDAO.findByEmail(foundUser.get().getEmail());
        }
        return Optional.empty();
    }
}
