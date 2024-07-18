package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.ClientDAO;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.ClientJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.ClientEntityMapper;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class ClientRepository implements ClientDAO {

    private final ClientJpaRepository clientJpaRepository;
    private final ClientEntityMapper clientEntityMapper;

    @Override
    public void updateClientInformation(Client client, Integer id) {
        ClientEntity clientEntity = findClientEntityById(id);

        clientEntity.setName(client.getName());
        clientEntity.setSurname(client.getSurname());
        clientEntity.setPhone(client.getPhone());

        clientJpaRepository.saveAndFlush(clientEntity);
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return clientJpaRepository
                .findById(id)
                .map(clientEntityMapper::mapFromEntity);
    }

    @Override
    public void deleteClientById(Integer userId) {
        clientJpaRepository.deleteById(userId);
    }

    @Override
    public ClientEntity createClient(ClientEntity clientEntity) {
        return clientJpaRepository.saveAndFlush(clientEntity);
    }


    public ClientEntity findClientEntityById(Integer userId) {
        return clientJpaRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Could not find user with id: [%s]"
                                .formatted(userId)
                ));
    }

    @Override
    public Optional<Client> findByUserId(Integer supperUserId) {
        Optional<ClientEntity> clientEntity = clientJpaRepository.findBySupperUser_SupperUserId(supperUserId);
        if (clientEntity.isPresent()) {
            return clientEntity.map(clientEntityMapper::mapFromEntity);
        }
        return Optional.empty();
    }

    @Override
    public Client detachUserFromRestaurant(String email) {
        Optional<ClientEntity> bySupperUserEmail = clientJpaRepository.findBySupperUser_Email(email);

        if (bySupperUserEmail.isPresent()) {
            ClientEntity client = bySupperUserEmail.get();
            client.setIsShown(false);
            client.setSupperUser(null);
            clientJpaRepository.saveAndFlush(client);
            return clientEntityMapper.mapFromEntity(client);
        } else {
            throw new EntityNotFoundException("Could not find restaurant");
        }
    }
}
