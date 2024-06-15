package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.controller.utils.NameBeautifier;
import pl.Aevise.SupperSpeed.api.dto.DeliveryAddressDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.DeliveryAddressMapper;
import pl.Aevise.SupperSpeed.business.dao.DeliveryAddressDAO;
import pl.Aevise.SupperSpeed.business.dao.DeliveryAddressListDAO;
import pl.Aevise.SupperSpeed.business.utils.DeliveryAddressPageFilter;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressListDAO deliveryAddressListDAO;
    private final DeliveryAddressMapper deliveryAddressMapper;

    private final DeliveryAddressDAO deliveryAddressDAO;

    @Transactional
    public Page<DeliveryAddressList> getAllDeliveryAddressesByRestaurantId(Integer restaurantId, PageRequest pageRequest) {
        Page<DeliveryAddressList> deliveryAddressLists = deliveryAddressListDAO.getAllDeliveryAddressesByRestaurantId(restaurantId, pageRequest);
        if (!deliveryAddressLists.isEmpty()) {
            log.info("Successfully fetched page: [{}]/[{}]", deliveryAddressLists.getNumber() + 1, deliveryAddressLists.getTotalPages());
            return deliveryAddressLists;
        }
        log.warn("Restaurant with id [{}] does not have delivery addresses", restaurantId);
        return Page.empty();
    }

    @Transactional
    public void removeDeliveryAddress(Integer deliveryAddressId, Integer restaurantId) {

        DeliveryAddressKey deliveryAddressKey = buildDeliveryAddressKey(deliveryAddressId, restaurantId);
        deliveryAddressListDAO.removeDeliveryAddress(deliveryAddressKey);
    }

    @Transactional
    public void addDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO, Integer restaurantId) {
        DeliveryAddress deliveryAddress;
        Integer deliveryAddressId;
        DeliveryAddress newDeliveryAddress = deliveryAddressMapper.mapFromDTO(deliveryAddressDTO);

        Optional<DeliveryAddress> deliveryAddress1 = deliveryAddressDAO.checkIfDeliveryAddressExist(newDeliveryAddress);
        if (deliveryAddress1.isPresent()) {
            deliveryAddress = deliveryAddress1.get();
            deliveryAddressId = deliveryAddress.getDeliveryAddressId();

            log.info("delivery address already exists in the database. Id: [{}]", deliveryAddressId);
            if (checkIfRelationAlreadyExist(restaurantId, deliveryAddress)) {
                log.info("Relation between restaurant [{}] and delivery address [{}] already exists!",
                        restaurantId,
                        deliveryAddressId);
                return;
            }
        } else {
            newDeliveryAddress = beautifyNames(newDeliveryAddress);
            deliveryAddress = deliveryAddressDAO.saveNewDeliveryAddress(newDeliveryAddress);
            log.info("added new delivery address to database. Id: [{}]", deliveryAddress.getDeliveryAddressId());
        }
        deliveryAddressId = deliveryAddress.getDeliveryAddressId();
        DeliveryAddressListEntity deliveryAddressListEntity = buildDeliveryAddressListEntity(deliveryAddressId, restaurantId);
        deliveryAddressListDAO.addNewRestaurantToDeliveryAddress(deliveryAddressListEntity);
        log.info("Connected delivery address [{}] with restaurant [{}]",
                deliveryAddressListEntity.getDeliveryAddressEntity().getDeliveryAddressId(),
                deliveryAddressListEntity.getRestaurantEntity().getId());
    }

    @Transactional
    public Page<DeliveryAddressDTO> getAddressesWithoutDeliveryBasedOnPostalCode(Integer restaurantId, DeliveryAddressDTO deliveryAddressDTO, PageRequest pageRequest) {
        String postalCode = deliveryAddressDTO.getPostalCode();

        List<DeliveryAddress> addresses = deliveryAddressListDAO.getAllDeliveryAddressesByRestaurantId(restaurantId);

        List<DeliveryAddress> addressesForPostalCode = deliveryAddressDAO.getAllByPostalCode(deliveryAddressDTO.getPostalCode());

        if (addressesForPostalCode.isEmpty()) {
            log.info("No addresses with postal code: [{}] added", postalCode);
            return Page.empty();
        }
        if (addresses.isEmpty()) {
            log.info("Restaurant can deliver to [{}] more places nearby", addressesForPostalCode.size());
            return DeliveryAddressPageFilter.convertListToPage(addressesForPostalCode, pageRequest)
                    .map(deliveryAddressMapper::mapToDTO);
        }

        Page<DeliveryAddress> filteredList = DeliveryAddressPageFilter
                .filterAddressesNotInRestaurantDeliveryList(
                        addressesForPostalCode,
                        addresses,
                        pageRequest
                );

        if (filteredList.isEmpty()) {
            log.info("All [{}] addresses has been added", addressesForPostalCode.size());
            return Page.empty();
        } else {
            log.info("[{}] More addresses can be added to delivery list", filteredList.getNumberOfElements());
            return filteredList
                    .map(deliveryAddressMapper::mapToDTO);
        }
    }

    private boolean checkIfRelationAlreadyExist(Integer restaurantId, DeliveryAddress deliveryAddress) {
        Optional<DeliveryAddressList> byRestaurantAndAddress = deliveryAddressListDAO
                .getByRestaurantAndAddress(buildDeliveryAddressListEntity(
                        deliveryAddress.getDeliveryAddressId(),
                        restaurantId
                ));

        return byRestaurantAndAddress.isPresent();
    }

    public List<DeliveryAddressDTO> separateAddresses(Page<DeliveryAddressList> deliveryAddressLists) {
        List<DeliveryAddressDTO> deliveryAddress = new ArrayList<>();

        if (!deliveryAddressLists.isEmpty()) {
            log.info("Separating [{}] addresses", deliveryAddressLists.getNumberOfElements());
            for (DeliveryAddressList deliveryAddressList : deliveryAddressLists) {
                deliveryAddress
                        .add(deliveryAddressMapper
                                .mapToDTO(deliveryAddressList.getDeliveryAddress()));
            }
            return deliveryAddress;
        }
        log.warn("No addresses to separate. List is empty");
        return List.of();
    }

    public List<Restaurant> getRestaurantsDeliveringOnAddress(String city, String streetName) {
        List<Restaurant> restaurants = deliveryAddressListDAO.getAllByCityAndStreetName(city, streetName);

        if(!restaurants.isEmpty()){
            log.info("Found [{}] restaurants delivering to address [{}], [{}]", restaurants.size(), city, streetName);
            return restaurants;
        }
        log.info("Did not found restaurants delivering to address [{}], [{}]", city, streetName);
        return List.of();
    }

    public List<Restaurant> getRestaurantsDeliveringOnAddressByCuisine(String city, String streetName, String cuisine) {
        List<Restaurant> restaurants = deliveryAddressListDAO.getAllByCityAndStreetNameByCuisine(city, streetName, cuisine);

        if(!restaurants.isEmpty()){
            log.info("Found [{}] restaurants delivering to address [{}], [{}]", restaurants.size(), city, streetName);
            return restaurants;
        }
        log.info("Did not found restaurants delivering to address [{}], [{}]", city, streetName);
        return List.of();
    }

    private DeliveryAddressKey buildDeliveryAddressKey(Integer deliveryAddressId, Integer restaurantId) {
        return DeliveryAddressKey.builder()
                .deliveryAddressId(deliveryAddressId)
                .restaurantId(restaurantId)
                .build();
    }


    private DeliveryAddressListEntity buildDeliveryAddressListEntity(Integer deliveryAddressId, Integer restaurantId) {
        return DeliveryAddressListEntity.builder()
                .id(DeliveryAddressKey.builder()
                        .deliveryAddressId(deliveryAddressId)
                        .restaurantId(restaurantId)
                        .build())
                .deliveryAddressEntity(DeliveryAddressEntity.builder()
                        .deliveryAddressId(deliveryAddressId)
                        .build())
                .restaurantEntity(
                        RestaurantEntity.builder()
                                .id(restaurantId)
                                .build()
                )
                .build();
    }


    private DeliveryAddress beautifyNames(DeliveryAddress newDeliveryAddress) {
        return newDeliveryAddress
                .withCity(NameBeautifier.handleName(newDeliveryAddress.getCity()))
                .withCountry(NameBeautifier.handleName(newDeliveryAddress.getCountry()))
                .withStreetName(NameBeautifier.handleName(newDeliveryAddress.getStreetName()))
                .withDistrict(NameBeautifier.handleName(newDeliveryAddress.getDistrict()));
    }
}
