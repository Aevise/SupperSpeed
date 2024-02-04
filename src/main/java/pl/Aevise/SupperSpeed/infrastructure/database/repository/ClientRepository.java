package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.ClientMapper;
import pl.Aevise.SupperSpeed.business.dao.ClientDAO;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.ClientJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.ClientEntityMapper;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.jpa.SupperUserJpaRepository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class ClientRepository implements ClientDAO {

    private final SupperUserJpaRepository supperUserJpaRepository;
    private final ClientJpaRepository clientJpaRepository;
    private final ClientEntityMapper clientEntityMapper;
    private final ClientMapper clientMapper;

    @Override
    public Optional<Client> findByEmail(String email) {
        Optional<SupperUserEntity> userEntity = supperUserJpaRepository.findByEmail(email);

        return userEntity
                .flatMap(supperUserEntity ->
                        clientJpaRepository
                                .findById(supperUserEntity.getSupperUserId())
                                .map(clientEntityMapper::mapFromEntity));
    }

    @Override
    public void updateClientInformation(ClientDTO clientDTO, Integer id) {
        ClientEntity clientEntity = findClientEntityById(id);

        Client clientNewData = clientMapper.mapFromDTO(clientDTO);

        clientEntity.setName(clientNewData.getName());
        clientEntity.setSurname(clientNewData.getSurname());
        clientEntity.setPhone(clientNewData.getPhone());

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


    public ClientEntity findClientEntityById(Integer userId) {
        return clientJpaRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Could not find user with id: [%s]"
                                .formatted(userId)
                ));
    }
}
