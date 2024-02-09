package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
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
    private final AddressService addressService;

    @Transactional
    public void deleteRestaurantById(Integer id) {
        restaurantDAO.deleteRestaurantById(id);
        log.info("Deleted restaurant with id: [{}]", id);
    }

    @Transactional
    public Optional<Restaurant> findRestaurantByEmail(String email) {
        Optional<SupperUser> foundUser = profileService.findUserByEmail(email);

        if (foundUser.isPresent()) {
            return restaurantDAO.findByEmail(foundUser.get().getEmail());
        }
        return Optional.empty();
    }

    public void updateAddress(AddressDTO addressDTO, String currentUserName) {
        Optional<SupperUser> userByEmail = profileService.findUserByEmail(currentUserName);

        if (userByEmail.isPresent()) {
            Integer userId = userByEmail.get().getSupperUserId();
            addressService.updateAddressByUserId(addressDTO, userId);
            log.info("Updated address for user with id: [{}]", userId);
        } else {
            log.error("Could not update address for user with name: [{}]. User not found", currentUserName);
        }
    }

    public void updateRestaurantInformation(RestaurantDTO restaurantDTO, String email) {
        Optional<SupperUser> userByEmail = profileService.findUserByEmail(email);

        if (userByEmail.isPresent()) {
            Integer userId = userByEmail.get().getSupperUserId();
            restaurantDAO.updateRestaurantInformation(restaurantDTO, userId);
            log.info("Restaurant's [{}] information updated successfully.", userId);
        } else {
            log.error("Could not update information for restaurant: [{}]. Client not found.", email);
        }
    }
}
