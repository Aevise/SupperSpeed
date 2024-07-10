package pl.Aevise.SupperSpeed.business.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.deliveryAddress1;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.deliveryAddress2;

@ExtendWith(MockitoExtension.class)
class DeliveryAddressPageFilterMockitoTest {

    @Test
    void filterAddressesNotInRestaurantDeliveryList() {
        //given
        List<DeliveryAddress> addressesWhereRestaurantDoesNotDeliver = List.of(deliveryAddress1(), deliveryAddress2());
        List<DeliveryAddress> addressesWhereRestaurantDelivers = List.of(deliveryAddress1());
        PageImpl<DeliveryAddress> expected = new PageImpl<>(List.of(deliveryAddress2()));
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("streetName").ascending());

        //when
        Page<DeliveryAddress> result = DeliveryAddressPageFilter.filterAddressesNotInRestaurantDeliveryList(
                addressesWhereRestaurantDoesNotDeliver,
                addressesWhereRestaurantDelivers,
                pageRequest
        );

        //then
        assertThat(result.toList()).isEqualTo(expected.toList());
    }
}