package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.business.dao.AddressDAO;
import pl.Aevise.SupperSpeed.business.dao.ClientDAO;
import pl.Aevise.SupperSpeed.business.dao.RestaurantDAO;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.domain.Restaurant;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AddressService {

    private final ClientDAO clientDAO;
    private final AddressDAO addressDAO;
private final RestaurantDAO restaurantDAO;
    @Qualifier("UpdateAddress")
    public void updateAddressByUserId(AddressDTO addressDTO, Integer userId){
        Optional<Client> client = clientDAO.findById(userId);

        if(client.isPresent()){
            Integer addressId = client.get().getAddress().getAddressId();
            addressDAO.updateAddress(addressDTO, addressId);
            return;
        }

        Optional<Restaurant> restaurant = restaurantDAO.findById(userId);
        if(restaurant.isPresent()){
            Integer addressId = restaurant.get().getAddress().getAddressId();
            addressDAO.updateAddress(addressDTO, addressId);
        }

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
