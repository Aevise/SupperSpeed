package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.business.dao.AddressDAO;
import pl.Aevise.SupperSpeed.business.dao.ClientDAO;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.domain.Client;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AddressService {

    private final ClientDAO clientDAO;
    private final AddressDAO addressDAO;

    @Qualifier("UpdateAddress")
    public void updateAddress(AddressDTO addressDTO, Integer userId){
        Optional<Client> client = clientDAO.findById(userId);

        Integer addressId = client.get().getAddress().getAddressId();
        addressDAO.updateAddress(addressDTO, addressId);

    }

    @Transactional
    public Optional<Address> findById(Integer id){
        Optional<Address> address = addressDAO.findById(id);

        if (address.isPresent()){
            return address;
        }
        return Optional.empty();
    }
}
