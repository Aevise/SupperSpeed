package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.business.dao.AddressDAO;
import pl.Aevise.SupperSpeed.business.dao.ClientDAO;
import pl.Aevise.SupperSpeed.business.dao.RestaurantDAO;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.AddressEntityMapper;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AddressService {

    private final ClientDAO clientDAO;
    private final AddressDAO addressDAO;
    private final RestaurantDAO restaurantDAO;

    private final AddressMapper addressMapper;
    private final AddressEntityMapper addressEntityMapper;

    @Qualifier("UpdateAddress")
    public void updateAddressByUserId(AddressDTO addressDTO, Integer userId) {
        Optional<Client> client = clientDAO.findById(userId);

        if (client.isPresent()) {
            Integer addressId = client.get().getAddress().getAddressId();
            log.info("Fetched client with id: [{}]", userId);

            Address address = addressMapper.mapFromDTO(addressDTO);
            addressDAO.updateAddress(address, addressId);
            return;
        }

        Optional<Restaurant> restaurant = restaurantDAO.findById(userId);
        if (restaurant.isPresent()) {
            Integer addressId = restaurant.get().getAddress().getAddressId();
            log.info("Fetched restaurant with id: [{}]", userId);
            Address address = addressMapper.mapFromDTO(addressDTO);

            addressDAO.updateAddress(address, addressId);
        }

    }

    @Transactional
    public Optional<Address> findById(Integer id) {
        if (id == null) {
            log.warn("Tried to fetch address with id null");
            return Optional.empty();
        }

        Optional<Address> address = addressDAO.findById(id);
        if (address.isPresent()) {
            log.info("Found address with id: [{}]", id);
            return address;
        }
        log.warn("Could not find address with id: [{}]", id);
        return Optional.empty();
    }

    public List<Address> findAll() {
        List<Address> addresses = addressDAO.findAll();
        log.info("Found [{}] addresses", addresses.size());
        return addresses;
    }

    public List<String> findDistinctCities() {
        List<String> distinctCities = restaurantDAO.getDistinctCitiesWithRestaurants();

        if (!distinctCities.isEmpty()) {
            log.info("Found [{}] distinct cities", distinctCities.size());
            return distinctCities
                    .stream()
                    .sorted()
                    .toList();
        }
        log.warn("Could not find cities with restaurant");
        return List.of();
    }

    public AddressDTO getByRestaurantId(Integer restaurantId) {
        Optional<AddressEntity> addressByRestaurantId = restaurantDAO.getAddressByRestaurantId(restaurantId);
        if (addressByRestaurantId.isPresent()) {
            log.info("Found address for restaurant with id [{}]", restaurantId);
            Address address = addressEntityMapper.mapFromEntity(addressByRestaurantId.get());
            return addressMapper.mapToDTO(address);
        }
        log.info("Could not find address for restaurant with id [{}]", restaurantId);
        return null;
    }
}
