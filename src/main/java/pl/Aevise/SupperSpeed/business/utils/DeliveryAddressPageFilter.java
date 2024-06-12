package pl.Aevise.SupperSpeed.business.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;

import java.util.List;

public class DeliveryAddressPageFilter {

    public static Page<DeliveryAddress> filterAddressesNotInRestaurantDeliveryList(
            List<DeliveryAddress> addressesWhereRestaurantDoesNotDeliver,
            List<DeliveryAddress> addressesWhereRestaurantDelivers,
            PageRequest pageRequest
    ) {

        /**
         * Filters page with given predicate and returns new Page with original metadata
         */
        List<DeliveryAddress> filteredContent = addressesWhereRestaurantDoesNotDeliver.stream()
                .filter(address -> !addressesWhereRestaurantDelivers.contains(address))
                .toList();

        return new PageImpl<>(filteredContent,
                pageRequest,
                filteredContent.size()
        );
    }

    public static Page<DeliveryAddress> changeDeliveryAddressListToPage(List<DeliveryAddress> da) {
        return null;
    }
}
