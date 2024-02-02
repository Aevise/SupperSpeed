package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.business.dao.ClientDAO;
import pl.Aevise.SupperSpeed.domain.Client;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientDAO clientDAO;

//    @Transactional
//    public void updateClient(Client client, Integer id){
//        clientDAO.updateClientInformation(client, id);
//    }
}
