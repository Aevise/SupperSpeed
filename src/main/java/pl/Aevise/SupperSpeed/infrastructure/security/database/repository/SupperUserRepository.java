package pl.Aevise.SupperSpeed.infrastructure.security.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.domain.SupperUser;
import pl.Aevise.SupperSpeed.infrastructure.security.dao.SupperUserDAO;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.jpa.SupperUserJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.security.database.repository.mapper.SupperUserEntityMapper;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class SupperUserRepository implements SupperUserDAO {

    private final SupperUserJpaRepository supperUserJpaRepository;
    private final SupperUserEntityMapper supperUserEntityMapper;

    @Override
    public Optional<SupperUser> findByEmail(String email) {
        return supperUserJpaRepository.findByEmail(email)
                .map(supperUserEntityMapper::mapFromEntity);
    }

    @Override
    public void deleteUserByEmail(String email) {
        Optional<SupperUserEntity> user = supperUserJpaRepository.findByEmail(email);
        if (user.isPresent()) {
            supperUserJpaRepository.delete(user.get());
        } else throw new EntityNotFoundException(
                "Could not find user with email: [%s]".formatted(email)
        );
    }
}
