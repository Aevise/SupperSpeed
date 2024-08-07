package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;

import java.util.Optional;

public interface ClientDAO {

    void updateClientInformation(Client client, Integer id);

    Optional<Client> findById(Integer id);

    void deleteClientById(Integer userId);

    ClientEntity createClient(ClientEntity clientEntity);

    Optional<Client> findByUserId(Integer supperUserId);

    Client detachUserFromRestaurant(String email);
}
