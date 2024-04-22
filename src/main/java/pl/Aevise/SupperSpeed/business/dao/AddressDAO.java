package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.domain.Address;

import java.util.List;
import java.util.Optional;

public interface AddressDAO {
    Optional<Address> findById(Integer id);

    void updateAddress(Address address, Integer addressId);

    List<Address> findAll();

    List<String> findDistinctCities();

}
