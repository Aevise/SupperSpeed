package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.AddressDAO;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.AddressEntityMapper;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class AddressRepository implements AddressDAO {

    private final AddressJpaRepository addressJpaRepository;
    private final AddressEntityMapper addressEntityMapper;

    @Override
    public Optional<Address> findById(Integer id) {
        Optional<AddressEntity> addressEntity = addressJpaRepository.findById(id);

        if (addressEntity.isPresent()) {
            return addressJpaRepository
                    .findById(id)
                    .map(addressEntityMapper::mapFromEntity);
        }
        return Optional.empty();
    }
}
