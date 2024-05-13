package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.dao.AddressDAO;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.domain.SupperUser;
import pl.Aevise.SupperSpeed.domain.exception.NotFoundException;
import pl.Aevise.SupperSpeed.infrastructure.security.dao.SupperUserDAO;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final AddressDAO addressDAO;
    private final SupperUserDAO supperUserDAO;
    private final RestaurantService restaurantService;
    private final ClientService clientService;

    @Transactional
    public Optional<Address> getAddressById(Integer id) {
        Optional<Address> userAddress = addressDAO.findById(id);
        if (userAddress.isEmpty()) {
            String message = "Could not find address with user id: [%s]".formatted(id);
            log.error(message);
            throw new NotFoundException(message);
        }
        log.info("Retrieved address with id: [{}]", id);
        return userAddress;
    }

    @Transactional
    public void deleteUserByEmail(String email) {

        Optional<SupperUser> user = supperUserDAO.findByEmail(email);

        //TODO ukrywanie uzytkownikow, ktorzy maja wiecej niz jedno zamowienie
        if(user.isPresent()){
            restaurantService.detachUserFromRestaurant(email);
        }

        supperUserDAO.deleteUserByEmail(email);
//        if (user.isPresent()) {
//            Integer userId = user.get().getSupperUserId();
//            supperUserDAO.deleteUserById(userId);
//            //TODO ZROBIC RESTAURANT NA ISSHOWN = FALSE
//        } else {
//            log.error("Could not find the user with email: [{}] ", email);
//        }
    }

    public Optional<SupperUser> findUserByEmail(String email) {
        Optional<SupperUser> user = supperUserDAO.findByEmail(email);
        if (user.isPresent()) {
            log.info("Found user with email [{}]", email);
            return user;
        }
        log.warn("Could not find user with email [{}]", email);
        return Optional.empty();
    }
}
