package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.Aevise.SupperSpeed.domain.SupperUser;
import pl.Aevise.SupperSpeed.infrastructure.security.dao.SupperUserDAO;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileService {

    private final SupperUserDAO supperUserDAO;

    public Optional<SupperUser> findUserByEmail(String email){
        Optional<SupperUser> foundUser = supperUserDAO.findByEmail(email);
        foundUser.ifPresent(supperUser -> log.info(
                "Retrieved SupperUser id:[{}], email:[{}]",
                supperUser.getSupperUserId(),
                supperUser.getEmail()));
        return foundUser;
    }
}
