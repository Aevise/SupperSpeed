package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.ClientMapper;
import pl.Aevise.SupperSpeed.business.dao.ClientDAO;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.domain.SupperUser;
import pl.Aevise.SupperSpeed.infrastructure.security.dao.SupperUserDAO;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ClientProfileService {

    private final SupperUserDAO supperUserDAO;
    private final ClientDAO clientDAO;
    private final ClientMapper clientMapper;
    private final AddressService addressService;


    @Transactional
    public Optional<Client> findClientByEmail(String email) {
        Optional<SupperUser> foundUser = findUserByEmail(email);

        if (foundUser.isPresent()) {
            return clientDAO.findByEmail(foundUser.get().getEmail());
        }
        return Optional.empty();
    }

    @Transactional
    public void updateClientInformation(ClientDTO newUsersInformation, String email){
        Optional<SupperUser> currentUser = findUserByEmail(email);

        if(currentUser.isPresent()){
            Integer supperUserId = currentUser.get().getSupperUserId();
            log.info("Began updating user's: [{}] information", supperUserId);
            clientDAO.updateClientInformation(newUsersInformation, supperUserId);
        }
        else {
            log.error("Did not found user with email: [{}]", email);
        }
//        if(foundClient.isPresent()){
//            log.info("Retrieved client: [{}]", foundClient.get().getName());
//            Client client = foundClient.get();
//            buildClient(client);
//        }
    }

    @Transactional
    @Qualifier("clientUpdateAddress")
    public void updateAddress(AddressDTO addressDTO, String email){
        Optional<SupperUser> currentUser = findUserByEmail(email);

        if(currentUser.isPresent()) {
            Integer supperUserId = currentUser.get().getSupperUserId();
            log.info("Successfully retrieved user's: [{}] information", supperUserId);
            addressService.updateAddressByUserId(addressDTO, supperUserId);
        }
        else {
            log.error("Did not found user with email: [{}]", email);
        }
    }

    private Optional<SupperUser> findUserByEmail(String email){
        Optional<SupperUser> foundUser = supperUserDAO.findByEmail(email);
        foundUser.ifPresent(supperUser -> log.info(
                "Retrieved SupperUser id:[{}], email:[{}]",
                supperUser.getSupperUserId(),
                supperUser.getEmail()));
        return foundUser;
    }


    private static void buildClient(Client client) {
        Client.builder()
                .name(client.getName())
                .surname(client.getSurname())
                .phone(client.getPhone())
                .build();
    }

}
