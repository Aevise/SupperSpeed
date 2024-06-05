package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.controller.utils.NameBeautifier;
import pl.Aevise.SupperSpeed.api.dto.DeliveryAddressDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.DeliveryAddressMapper;
import pl.Aevise.SupperSpeed.business.dao.DeliveryAddressDAO;
import pl.Aevise.SupperSpeed.business.dao.DeliveryAddressListDAO;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;
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
    public List<DeliveryAddressDTO> getAllDeliveryAddressesByRestaurantId(Integer restaurantId) {
        List<DeliveryAddressList> deliveryAddressLists = deliveryAddressListDAO.getAllByRestaurantId(restaurantId);
        if (!deliveryAddressLists.isEmpty()) {
            return separateAddresses(deliveryAddressLists);
        }
        log.warn("Restaurant with id [{}] does not have delivery addresses", restaurantId);
        return List.of();
    }

    @Transactional
    public void removeDeliveryAddress(Integer deliveryAddressId, Integer restaurantId) {

        DeliveryAddressKey deliveryAddressKey = buildDeliveryAddressKey(deliveryAddressId, restaurantId);
        deliveryAddressListDAO.removeDeliveryAddress(deliveryAddressKey);
    }

    @Transactional
    public void addDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO, Integer restaurantId) {

        deliveryAddressListDAO.test("22-100", PageRequest.of(0, 2, Sort.by("deliveryAddressEntity.streetName").ascending()));


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
    public List<DeliveryAddressDTO> getAddressesWithoutDeliveryBasedOnPostalCode(Integer restaurantId, DeliveryAddressDTO deliveryAddressDTO) {
        String postalCode = deliveryAddressDTO.getPostalCode();

        List<DeliveryAddress> addresses = deliveryAddressListDAO.getAddressesWithoutDeliveryBasedOnPostalCode(restaurantId, deliveryAddressMapper.mapFromDTO(deliveryAddressDTO));
        if (!addresses.isEmpty()) {
            log.info("Found [{}] addresses where restaurant [{}] does not deliver for postal code [{}]",
                    addresses.size(),
                    restaurantId,
                    postalCode);

            return addresses.stream()
                    .map(deliveryAddressMapper::mapToDTO)
                    .toList();
        }
        log.info("Restaurant delivers to all addresses with postal code: [{}]", postalCode);
        return List.of();
    }

    private boolean checkIfRelationAlreadyExist(Integer restaurantId, DeliveryAddress deliveryAddress) {
        Optional<DeliveryAddressList> byRestaurantAndAddress = deliveryAddressListDAO
                .getByRestaurantAndAddress(buildDeliveryAddressListEntity(
                        deliveryAddress.getDeliveryAddressId(),
                        restaurantId
                ));

        return byRestaurantAndAddress.isPresent();
    }

    private List<DeliveryAddressDTO> separateAddresses(List<DeliveryAddressList> deliveryAddressLists) {
        List<DeliveryAddressDTO> deliveryAddress = new ArrayList<>();

        if (!deliveryAddressLists.isEmpty()) {
            log.info("Separating [{}] addresses", deliveryAddressLists.size());
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
