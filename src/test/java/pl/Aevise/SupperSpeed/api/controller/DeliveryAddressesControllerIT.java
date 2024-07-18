package pl.Aevise.SupperSpeed.api.controller;

import jakarta.persistence.EntityNotFoundException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import pl.Aevise.SupperSpeed.api.dto.DeliveryAddressDTO;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DeliveryAddressJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DeliveryAddressListJpaRepository;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;
import pl.Aevise.SupperSpeed.integration.configuration.FlywayManualMigrationsConfiguration;
import pl.Aevise.SupperSpeed.util.Constants;
import pl.Aevise.SupperSpeed.util.DTOFixtures;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.DeliveryAddressesController.*;

@AutoConfigureMockMvc
@Import(FlywayManualMigrationsConfiguration.class)
@WithMockUser(username = Constants.TEST_RESTAURANT_EMAIL_FLYWAY_1, password = Constants.testPassword, authorities = "RESTAURANT")
class DeliveryAddressesControllerIT extends AbstractITConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Flyway flyway;

    @Autowired
    private DeliveryAddressListJpaRepository deliveryAddressListJpaRepository;
    @Autowired
    private DeliveryAddressJpaRepository deliveryAddressJpaRepository;

    static Stream<Arguments> checkThatYouCanGetDeliveryAddressesForRestaurant() {
        return Stream.of(
                Arguments.of("0", 10),
                Arguments.of("1", 1)
        );
    }

    @BeforeEach
    void recreateFlywayMigrations() {
        flyway.clean();
        flyway.migrate();
    }

    @ParameterizedTest
    @MethodSource
    @SuppressWarnings("unchecked")
    void checkThatYouCanGetDeliveryAddressesForRestaurant(
            String currAdrPage,
            Integer expectedSize
    ) throws Exception {
        //given
        String currAdrSortingDirection = "asc";
        String exiAdrPage = "0";
        String exiAdrSortingDirection = "asc";

        //when
        ResultActions result = mockMvc.perform(get(SHOW_DELIVERY_ADDRESSES)
                .param("ca-page", currAdrPage)
                .param("ca-dir", currAdrSortingDirection)
                .param("ea-page", exiAdrPage)
                .param("ea-dir", exiAdrSortingDirection));

        var deliveryAddressesPage = (List<DeliveryAddressDTO>) Objects.requireNonNull(result.andReturn().getModelAndView()).getModel().get("addresses");
        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("addresses"))
                .andExpect(model().attributeExists("restaurantAddress"))
                .andExpect(model().attributeExists("addressesWithoutDelivery"))
                .andExpect(model().attributeExists("restaurantId"))
                .andExpect(model().attributeExists("totalNumberOfPages"))
                .andExpect(model().attributeExists("CAPage"))
                .andExpect(model().attributeExists("CASortingDirection"))
                .andExpect(model().attributeExists("EASortingDirection"))
                .andExpect(view().name("delivery_addresses"));

        assertEquals(deliveryAddressesPage.size(), expectedSize);
    }

    @Test
    void checkThatYouCanSuccessfullyRemoveDeliveryAddressFromRestaurant() throws Exception {
        //given
        int restaurantId = 3;
        int deliveryAddressId = 1;

        //when
        Optional<DeliveryAddressListEntity> shouldNotBeEmpty = deliveryAddressListJpaRepository.findById(DeliveryAddressKey.builder()
                .deliveryAddressId(deliveryAddressId)
                .restaurantId(restaurantId)
                .build());

        ResultActions result = mockMvc.perform(post(REMOVE_DELIVERY_ADDRESS)
                .param("deliveryAddressId", Integer.toString(deliveryAddressId))
                .param("restaurantId", Integer.toString(restaurantId)));

        Optional<DeliveryAddressListEntity> shouldBeEmpty = deliveryAddressListJpaRepository.findById(DeliveryAddressKey.builder()
                .deliveryAddressId(deliveryAddressId)
                .restaurantId(restaurantId)
                .build());

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + SHOW_DELIVERY_ADDRESSES));

        assertTrue(shouldNotBeEmpty.isPresent());
        assertTrue(shouldBeEmpty.isEmpty());
    }

    @Test
    void checkThatYouCanAddDeliveryAddressToRestaurant() throws Exception {
        //given
        int restaurantId = 3;
        DeliveryAddressDTO deliveryAddressDTO = DTOFixtures.deliveryAddressDTO1();

        deliveryAddressDTO.setDeliveryAddressId(null);

        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = deliveryAddressDTO.asMap();
        parametersMap.putIfAbsent("restaurantId", Integer.toString(restaurantId));
        parametersMap.forEach(parameters::add);

        //when
        ResultActions result = mockMvc.perform(post(ADD_DELIVERY_ADDRESS)
                .params(parameters));

        Optional<DeliveryAddressListEntity> shouldBePresent = deliveryAddressListJpaRepository.findById(DeliveryAddressKey.builder()
                .restaurantId(restaurantId)
                .deliveryAddressId(14)
                .build());

        Integer newDeliveryAddressId = shouldBePresent.orElseThrow(
                () -> new EntityNotFoundException("Delivery address not connected with restaurant")).getId().getDeliveryAddressId();
        DeliveryAddressEntity deliveryAddress = deliveryAddressJpaRepository.findById(newDeliveryAddressId).orElseThrow(
                () -> new EntityNotFoundException("Delivery Address not added"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + SHOW_DELIVERY_ADDRESSES));

        assertEquals(shouldBePresent.get().getId().getRestaurantId(), restaurantId);
        assertEquals(shouldBePresent.get().getId().getDeliveryAddressId(), deliveryAddress.getDeliveryAddressId());
        assertEquals(deliveryAddress.getCity(), deliveryAddressDTO.getCity());
        assertEquals(deliveryAddress.getCountry(), deliveryAddressDTO.getCountry());
        assertEquals(deliveryAddress.getDistrict(), deliveryAddressDTO.getDistrict());
        assertEquals(deliveryAddress.getStreetName(), deliveryAddressDTO.getStreetName());
        assertEquals(deliveryAddress.getPostalCode(), deliveryAddressDTO.getPostalCode());
    }
}