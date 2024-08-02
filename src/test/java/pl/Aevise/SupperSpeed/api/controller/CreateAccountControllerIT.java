package pl.Aevise.SupperSpeed.api.controller;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.api.controller.exception.UserNotFoundException;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.CuisineEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.ClientJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.CuisineJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.RestaurantJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.jpa.SupperUserJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.security.dto.SupperUserDTO;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;
import pl.Aevise.SupperSpeed.integration.configuration.FlywayManualMigrationsConfiguration;
import pl.Aevise.SupperSpeed.util.DTOFixtures;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.Aevise.SupperSpeed.api.controller.CreateAccountController.*;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.clientDTO1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.restaurantDTO1;

@AutoConfigureMockMvc
@Import(FlywayManualMigrationsConfiguration.class)
class CreateAccountControllerIT extends AbstractITConfiguration {

    @Autowired
    Flyway flyway;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CuisineJpaRepository cuisineJpaRepository;
    @Autowired
    private SupperUserJpaRepository supperUserJpaRepository;
    @Autowired
    private ClientJpaRepository clientJpaRepository;
    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void recreateFlywayMigrations() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void checkThatYouCanGetAccountCreationForm() {
        //given
        String url = String.format("http://localhost:%s%s" + CREATE_ACCOUNT_PAGE, port, basePath);

        //when
        List<CuisineEntity> allCuisines = cuisineJpaRepository.findAll()
                .stream().filter(cuisine -> !cuisine.getCuisine().equals("All")).toList();
        ResponseEntity<String> page = testRestTemplate.getForEntity(url, String.class);
        String content = page.getBody();

        //then
        assertTrue(page.getStatusCode().is2xxSuccessful());
        for (CuisineEntity cuisine : allCuisines) {
            assert content != null;
            assertTrue(content.contains(cuisine.getCuisine()));
        }
    }

    @Test
    @Transactional
    void checkThatYouCanCreateClientUser() {
        //given
        String url = String.format("http://localhost:%s%s" + CREATE_ACCOUNT_USER, port, basePath);
        SupperUserDTO supperUserDTO = DTOFixtures.supperUserDTO1();
        supperUserDTO.setSupperUserId(null);
        ClientDTO clientDTO = clientDTO1();
        clientDTO.setSupperUserId(null);
        clientDTO.setId(null);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = clientDTO.asMap();
        parametersMap.putAll(supperUserDTO.asMap());
        parametersMap.putIfAbsent("role_id", "1");
        parametersMap.putIfAbsent("password", "test");
        parametersMap.forEach(body::add);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        //when
        ResponseEntity<String> response = testRestTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        SupperUserEntity newSupperUser = supperUserJpaRepository.findById(5).orElseThrow(
                () -> new UserNotFoundException("User not added"));
        ClientEntity newClient = clientJpaRepository.findById(2).orElseThrow(
                () -> new UserNotFoundException("Client not added"));

        //then
        assertTrue(response.getStatusCode().is3xxRedirection());
        assertEquals(clientDTO.getName(), newClient.getName());
        assertEquals(clientDTO.getSurname(), newClient.getSurname());
        assertEquals(clientDTO.getPhone(), newClient.getPhone());
        assertEquals(supperUserDTO.getEmail(), newSupperUser.getEmail());
    }

    @Test
    @Transactional
    void checkThatYouCanCreateRestaurantUser() {
        //given
        String url = String.format("http://localhost:%s%s" + CREATE_ACCOUNT_RESTAURANT, port, basePath);
        SupperUserDTO supperUserDTO = DTOFixtures.supperUserDTO1();
        supperUserDTO.setSupperUserId(null);
        RestaurantDTO restaurantDTO = restaurantDTO1();
        restaurantDTO.setUserId(null);
        restaurantDTO.setRestaurantId(null);
        restaurantDTO.setAddress(null);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = restaurantDTO.asMap();
        parametersMap.putAll(supperUserDTO.asMap());
        parametersMap.putIfAbsent("role_id", "2");
        parametersMap.putIfAbsent("password", "test");
        parametersMap.putIfAbsent("cuisine", "Italian");
        parametersMap.forEach(body::add);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        //when
        ResponseEntity<String> response = testRestTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        SupperUserEntity newSupperUser = supperUserJpaRepository.findById(5).orElseThrow(
                () -> new UserNotFoundException("User not added"));
        RestaurantEntity newRestaurant = restaurantJpaRepository.findById(4).orElseThrow(
                () -> new UserNotFoundException("Restaurant not added"));

        //then
        assertTrue(response.getStatusCode().is3xxRedirection());
        assertEquals(restaurantDTO.getRestaurantName(), newRestaurant.getRestaurantName());
        assertEquals(restaurantDTO.getOpenHour(), newRestaurant.getOpenHour());
        assertEquals(restaurantDTO.getCloseHour(), newRestaurant.getCloseHour());
        assertEquals(restaurantDTO.getPhone(), newRestaurant.getPhone());
        assertEquals(supperUserDTO.getEmail(), newSupperUser.getEmail());
    }
}