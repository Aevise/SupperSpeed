package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.business.dao.ClientDAO;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.domain.SupperUser;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ClientProfileService {

    private final ClientDAO clientDAO;
    private final AddressService addressService;
    private final ProfileService profileService;


    @Transactional
    public Optional<Client> findClientByEmail(String email) {
        Optional<SupperUser> foundUser = profileService.findUserByEmail(email);

        if (foundUser.isPresent()) {
            return clientDAO.findByEmail(foundUser.get().getEmail());
        }
        return Optional.empty();
    }

    @Transactional
    public void updateClientInformation(ClientDTO newUsersInformation, String email){
        Optional<SupperUser> currentUser = profileService.findUserByEmail(email);

        if(currentUser.isPresent()){
            Integer supperUserId = currentUser.get().getSupperUserId();
            log.info("Began updating user's: [{}] information", supperUserId);
            clientDAO.updateClientInformation(newUsersInformation, supperUserId);
        }
        else {
            log.error("Did not found user with email: [{}]", email);
        }
    }

    @Transactional
    @Qualifier("clientUpdateAddress")
    public void updateAddress(AddressDTO addressDTO, String email){
        Optional<SupperUser> currentUser = profileService.findUserByEmail(email);

        if(currentUser.isPresent()) {
            Integer supperUserId = currentUser.get().getSupperUserId();
            log.info("Successfully retrieved user's: [{}] information", supperUserId);
            addressService.updateAddressByUserId(addressDTO, supperUserId);
        }
        else {
            log.error("Did not found user with email: [{}]", email);
        }
    }




    private static void buildClient(Client client) {
        Client.builder()
                .name(client.getName())
                .surname(client.getSurname())
                .phone(client.getPhone())
                .build();
    }

}
