package pl.Aevise.SupperSpeed.api.controller;

import jakarta.persistence.EntityNotFoundException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;
import pl.Aevise.SupperSpeed.integration.configuration.FlywayManualMigrationsConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.RestaurantProfileController.*;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_FLYWAY_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.addressDTO1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.restaurantDTO1;

@AutoConfigureMockMvc
@Import(FlywayManualMigrationsConfiguration.class)
@WithMockUser(username = TEST_RESTAURANT_EMAIL_FLYWAY_1, authorities = "RESTAURANT")
class RestaurantProfileControllerIT extends AbstractITConfiguration {

    @Autowired
    Flyway flyway;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;
    @Autowired
    private AddressJpaRepository addressJpaRepository;

    public static Stream<Arguments> checkThatRestaurantVisibilityChanges() {
        return Stream.of(
                Arguments.of(true, false),
                Arguments.of(false, true)
        );
    }

    @BeforeEach
    void recreateFlywayMigrations() {
        flyway.clean();
        flyway.migrate();
    }

    @AfterEach
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void deleteTestPhotosFromFolder() throws IOException {
        String TEST_IMAGE_FOLDER = "images/" + restaurantDTO1().getRestaurantName() + "_3";
        File directory = new File(TEST_IMAGE_FOLDER);
        Path pathToDirectory = Path.of(TEST_IMAGE_FOLDER);

        if (directory.exists()) {
            try (Stream<Path> files = Files.walk(pathToDirectory)) {
                files
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
        assertFalse(false, String.valueOf(Files.exists(pathToDirectory)));
    }

    @Test
    void checkThatYouCanGetRestaurantMenu() throws Exception {
        //given
        int expectedRestaurantId = 3;
        int expectedAddressId = 3;

        RestaurantEntity restaurant = restaurantJpaRepository.findById(expectedRestaurantId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));
        AddressEntity address = addressJpaRepository.findById(expectedAddressId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));

        //when
        ResultActions result = mockMvc.perform(get(RESTAURANT_PROFILE));
        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("restaurantDTO"))
                .andExpect(model().attributeExists("addressDTO"))
                .andExpect(model().attributeExists("restaurantId"))
                .andExpect(view().name("restaurant_profile"));

        assertNotNull(content);
        assertTrue(content.contains(restaurant.getRestaurantName()));
        assertTrue(content.contains(address.getCountry()));
        assertTrue(content.contains(address.getCity()));
        assertTrue(content.contains(address.getPostalCode()));
        assertTrue(content.contains(address.getStreetName()));
        assertTrue(content.contains(address.getBuildingNumber()));
    }

    @Test
    void checkThatYouCanUpdateRestaurantInformation() throws Exception {
        //given
        int restaurantId = 3;
        String action = "updateProfile";

        RestaurantDTO restaurantDTO = restaurantDTO1();
        restaurantDTO.setRestaurantId(restaurantId);
        restaurantDTO.setUserId(3);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = restaurantDTO.asMap();
        parametersMap.putIfAbsent("action", action);
        parametersMap.putIfAbsent("restaurantId", String.valueOf(restaurantId));
        parametersMap.putIfAbsent("oldName", "restaurant3");
        parametersMap.forEach(parameters::add);

        //when
        ResultActions result = mockMvc.perform(post(RESTAURANT_UPDATE).
                params(parameters));

        RestaurantEntity newRestaurant = restaurantJpaRepository.findById(restaurantId).orElseThrow(
                () -> new EntityNotFoundException("Restaurant deleted?"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:" + RESTAURANT_PROFILE))
                .andExpect(model().hasNoErrors());
        assertEquals(restaurantDTO.getRestaurantName(), newRestaurant.getRestaurantName());
        assertEquals(restaurantDTO.getPhone(), newRestaurant.getPhone());
    }

    @Test
    void checkThatYouCanUpdateRestaurantAddress() throws Exception {
        //given
        int restaurantId = 3;
        int addressId = 3;
        String action = "updateAddress";

        AddressDTO addressDTO = addressDTO1();
        addressDTO.setAddressId(addressId);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = addressDTO.asMap();
        parametersMap.putIfAbsent("action", action);
        parametersMap.putIfAbsent("restaurantId", String.valueOf(restaurantId));
        parametersMap.putIfAbsent("oldName", "restaurant3");
        parametersMap.forEach(parameters::add);

        //when
        ResultActions result = mockMvc.perform(post(RESTAURANT_UPDATE).
                params(parameters));

        AddressEntity newAddress = addressJpaRepository.findById(addressId).orElseThrow(
                () -> new EntityNotFoundException("Address deleted?"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:" + RESTAURANT_PROFILE))
                .andExpect(model().hasNoErrors());

        assertEquals(addressDTO.getCountry(), newAddress.getCountry());
        assertEquals(addressDTO.getCity(), newAddress.getCity());
        assertEquals(addressDTO.getPostalCode(), newAddress.getPostalCode());
        assertEquals(addressDTO.getStreetName(), newAddress.getStreetName());
        assertEquals(addressDTO.getBuildingNumber(), newAddress.getBuildingNumber());
        assertEquals(addressDTO.getLocalNumber(), newAddress.getLocalNumber());
    }

    @ParameterizedTest
    @MethodSource
    void checkThatRestaurantVisibilityChanges(
            Boolean givenAvailability,
            Boolean expectedAvailability
    ) throws Exception {
        //given
        int restaurantId = 3;

        RestaurantEntity restaurant = restaurantJpaRepository.findById(restaurantId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));
        restaurant.setIsShown(givenAvailability);
        restaurantJpaRepository.saveAndFlush(restaurant);

        //when
        ResultActions result = mockMvc.perform(post(RESTAURANT_TOGGLE).
                param("restaurantId", String.valueOf(restaurantId)));

        RestaurantEntity newRestaurant = restaurantJpaRepository.findById(restaurantId).orElseThrow(
                () -> new EntityNotFoundException("Restaurant deleted?"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:" + RESTAURANT_PROFILE))
                .andExpect(model().hasNoErrors());

        assertEquals(newRestaurant.getIsShown(), expectedAvailability);
    }
}