package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.DeliveryAddressDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.DeliveryAddressMapper;
import pl.Aevise.SupperSpeed.business.dao.DeliveryAddressDAO;
import pl.Aevise.SupperSpeed.business.dao.DeliveryAddressListDAO;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
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
    public void deleteDeliveryAddressById(Integer deliveryAddressId, Integer restaurantId) {
        try {
            deliveryAddressDAO.deleteDeliveryAddressById(deliveryAddressId);
        } catch (HibernateException e) {
            log.warn("Could not delete address with id: [{}]", deliveryAddressId, e);
            return;
        }

        DeliveryAddressKey deliveryAddressKey = buildDeliveryAddressKey(deliveryAddressId, restaurantId);
        deliveryAddressListDAO.deleteByAddressAndRestaurantId(deliveryAddressKey);
    }

    @Transactional
    public void addDeliveryAddress(DeliveryAddressDTO deliveryAddressDTO, Integer restaurantId) {
        DeliveryAddress deliveryAddress;
        DeliveryAddress newDeliveryAddress = deliveryAddressMapper.mapFromDTO(deliveryAddressDTO);

        Optional<DeliveryAddress> deliveryAddress1 = deliveryAddressDAO.checkIfDeliveryAddressExist(newDeliveryAddress);
        if(deliveryAddress1.isPresent()){
            deliveryAddress = deliveryAddress1.get();
            log.info("delivery address already exists in the database. Id: [{}]", deliveryAddress.getDeliveryAddressId());
        }else {
            deliveryAddress = deliveryAddressDAO.saveNewDeliveryAddress(newDeliveryAddress);
            log.info("added new delivery address to database. Id: [{}]", deliveryAddress.getDeliveryAddressId());
        }
        DeliveryAddressList deliveryAddressList = buildDeliveryAddressList(deliveryAddress, restaurantId);
        deliveryAddressListDAO.addNewRestaurantToDeliveryAddress(deliveryAddressList);
        log.info("Connected delivery address [{}] with restaurant [{}]",
                deliveryAddressList.getDeliveryAddress().getDeliveryAddressId(),
                deliveryAddressList.getRestaurantId());
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

    private DeliveryAddressList buildDeliveryAddressList(DeliveryAddress deliveryAddress, Integer restaurantId){
        return DeliveryAddressList.builder()
                .deliveryAddress(deliveryAddress)
                .restaurantId(restaurantId)
                .build();
    }
}
