package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.business.dao.ClientDAO;
import pl.Aevise.SupperSpeed.domain.Client;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ClientService {

    private final ClientDAO clientDAO;

    @Transactional
    public Optional<Client> findById(Integer id){
        return clientDAO.findById(id);
    }

    public void deleteClientById(Integer userId) {
        clientDAO.deleteClientById(userId);
        log.info("Deleted user with id: [{}]", userId);
    }

//    @Transactional
//    public void updateClient(Client client, Integer id){
//        clientDAO.updateClientInformation(client, id);
//    }
}
