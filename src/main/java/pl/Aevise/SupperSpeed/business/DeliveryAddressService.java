package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.Aevise.SupperSpeed.api.dto.DeliveryAddressDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.DeliveryAddressMapper;
import pl.Aevise.SupperSpeed.business.dao.DeliveryAddressListDAO;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressListDAO deliveryAddressListDAO;
    private final DeliveryAddressMapper deliveryAddressMapper;

    public List<DeliveryAddressDTO> getAllDeliveryAddressesByRestaurantId(Integer restaurantId) {
        List<DeliveryAddressList> deliveryAddressLists = deliveryAddressListDAO.getAllByRestaurantId(restaurantId);
        if(!deliveryAddressLists.isEmpty()){
            return separateAddresses(deliveryAddressLists);
        }
        log.warn("Restaurant with id [{}] does not have delivery addresses", restaurantId);
        return List.of();
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

}
