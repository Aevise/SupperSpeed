package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.ClientDAO;
import pl.Aevise.SupperSpeed.domain.Client;
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

    @Override
    public Optional<Client> findByEmail(String email) {
        Optional<SupperUserEntity> user = supperUserJpaRepository.findByEmail(email);

        return user
                .flatMap(supperUserEntity ->
                        clientJpaRepository
                                .findById(supperUserEntity.getSupperUserId())
                                .map(clientEntityMapper::mapFromEntity));
    }
}
