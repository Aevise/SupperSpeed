package pl.Aevise.SupperSpeed.api.controller;

import jakarta.persistence.EntityNotFoundException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.ClientJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.jpa.SupperUserJpaRepository;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;
import pl.Aevise.SupperSpeed.integration.configuration.FlywayManualMigrationsConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.UserDeleteController.CLIENT_DELETE;
import static pl.Aevise.SupperSpeed.api.controller.UserDeleteController.CLIENT_LOGOUT;
import static pl.Aevise.SupperSpeed.util.Constants.*;

@Import(FlywayManualMigrationsConfiguration.class)
@AutoConfigureMockMvc
class UserDeleteControllerIT extends AbstractITConfiguration {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Flyway flyway;
    @Autowired
    private SupperUserJpaRepository supperUserJpaRepository;
    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;
    @Autowired
    private ClientJpaRepository clientJpaRepository;

    @BeforeEach
    void recreateFlywayMigrations() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void checkThatYouCanDeleteRestaurant() throws Exception {
        //given
        int restaurantId = 3;
        int supperUserId = 3;

        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_FLYWAY_1).password(testPassword).authorities("RESTAURANT").build();
        RestaurantEntity restaurant = restaurantJpaRepository.findById(restaurantId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));
        SupperUserEntity supperUserEntity = supperUserJpaRepository.findById(supperUserId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));

        //when
        ResultActions result = mockMvc.perform(post(CLIENT_DELETE)
                .param("confirmation", "yes")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)));

        RestaurantEntity newRestaurant = restaurantJpaRepository.findById(restaurantId).orElseThrow(
                () -> new EntityNotFoundException("Restaurant fully deleted! It must not be deleted"));
        Optional<SupperUserEntity> shouldNotExist = supperUserJpaRepository.findById(supperUserId);

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + CLIENT_LOGOUT));
        assertTrue(shouldNotExist.isEmpty());
        assertEquals(restaurant.getId(), newRestaurant.getId());
        assertNull(newRestaurant.getSupperUser());
        assertFalse(newRestaurant.getIsShown());
    }

    @Test
    void checkThatYouCanDeleteClient() throws Exception {
        //given
        int clientId = 1;
        int supperUserId = 4;

        UserDetails userDetails = User.withUsername(TEST_CLIENT_EMAIL_FLYWAY_1).password(testPassword).authorities("CLIENT").build();
        ClientEntity client = clientJpaRepository.findById(clientId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));
        SupperUserEntity supperUserEntity = supperUserJpaRepository.findById(supperUserId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));

        //when
        ResultActions result = mockMvc.perform(post(CLIENT_DELETE)
                .param("confirmation", "yes")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)));

        ClientEntity newClient = clientJpaRepository.findById(clientId).orElseThrow(
                () -> new EntityNotFoundException("Restaurant fully deleted! It must not be deleted"));
        Optional<SupperUserEntity> shouldNotExist = supperUserJpaRepository.findById(supperUserId);

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + CLIENT_LOGOUT));
        assertTrue(shouldNotExist.isEmpty());
        assertEquals(client.getId(), newClient.getId());
        assertNull(newClient.getSupperUser());
        assertFalse(newClient.getIsShown());
    }
}