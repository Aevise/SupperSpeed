package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.business.dao.AddressDAO;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.AddressEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AddressRepository implements AddressDAO {

    private final AddressJpaRepository addressJpaRepository;
    private final AddressEntityMapper addressEntityMapper;
    private final AddressMapper addressMapper;

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

    @Override
    public void updateAddress(AddressDTO addressDTO, Integer addressId) {
        AddressEntity address = addressJpaRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Address with id: [%s] not found"
                                .formatted(addressId)
                ));
        Address newAddressData = addressMapper.mapFromDTO(addressDTO);

        address.setCurrentAddress(newAddressData.getCurrentAddress());
        address.setCity(newAddressData.getCity());
        address.setCountry(newAddressData.getCountry());
        address.setPostalCode(newAddressData.getPostalCode());

        addressJpaRepository.saveAndFlush(address);

    }

    @Override
    public List<Address> findAll() {
        return addressJpaRepository.findAll()
                .stream()
                .map(addressEntityMapper::mapFromEntity)
                .toList();
    }
}
