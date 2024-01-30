package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.business.dao.ClientDAO;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.domain.SupperUser;
import pl.Aevise.SupperSpeed.infrastructure.security.dao.SupperUserDAO;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ClientProfileViewService {

    private final SupperUserDAO supperUserDAO;
    private final ClientDAO clientDAO;

    @Transactional
    public Optional<Client> findByEmail(String email) {
        Optional<SupperUser> foundUser = supperUserDAO.findByEmail(email);
        foundUser.ifPresent(supperUser -> log.info(
                "Retrieved SupperUser id:[{}], email:[{}]",
                supperUser.getSupperUserId(),
                supperUser.getEmail()));

        if (foundUser.isPresent()) {
            return clientDAO.findByEmail(foundUser.get().getEmail());
        }
        return Optional.empty();
    }
}
