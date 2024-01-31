package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.business.dao.AddressDAO;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.domain.exception.NotFoundException;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserProfileService {

    private final AddressDAO addressDAO;

    @Transactional
    public Optional<Address> getAddressById(Integer id) {
        log.info("Trying to get address with user id: [{}]", id);
        Optional<Address> userAddress = addressDAO.findById(id);
        if (userAddress.isEmpty()) {
            throw new NotFoundException("Could not find address with user id: [%s]".formatted(id));
        }
        return userAddress;
    }
}
