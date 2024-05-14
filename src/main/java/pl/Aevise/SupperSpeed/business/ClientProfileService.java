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

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ClientProfileService {

    private final ClientDAO clientDAO;
    private final ClientMapper clientMapper;

    private final AddressService addressService;
    private final ProfileService profileService;

    private static void buildClient(Client client) {
        Client.builder()
                .name(client.getName())
                .surname(client.getSurname())
                .phone(client.getPhone())
                .build();
    }

    @Transactional
    public Optional<Client> findClientByEmail(String email) {
        Optional<SupperUser> foundUser = profileService.findUserByEmail(email);


        if (foundUser.isPresent()) {
            return clientDAO.findByUserId(foundUser.get().getSupperUserId());
        }
        return Optional.empty();
    }

    @Transactional
    public void updateClientInformation(ClientDTO clientDTO, Integer userId) {

        Client client = clientMapper.mapFromDTO(clientDTO);

        clientDAO.updateClientInformation(client, userId);
        log.info("Client's [{}] information updated successfully.", userId);
    }

    @Transactional
    @Qualifier("clientUpdateAddress")
    public void updateAddress(AddressDTO addressDTO, Integer userId) {
        addressService.updateAddressByUserId(addressDTO, userId);
        log.info("Successfully updated address for user: [{}]", userId);
    }

}
