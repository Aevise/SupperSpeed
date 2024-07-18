package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.domain.exception.NotFoundException;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.ClientJpaRepository;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.ClientProfileController.CLIENT_PROFILE;
import static pl.Aevise.SupperSpeed.api.controller.ClientProfileController.UPDATE_PROFILE;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_FLYWAY_1;
import static pl.Aevise.SupperSpeed.util.Constants.testPassword;

@AutoConfigureMockMvc
class ClientProfileControllerIT extends AbstractITConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClientJpaRepository clientJpaRepository;
    @Autowired
    private AddressJpaRepository addressJpaRepository;

    @Test
    void checkThatYouAreRedirectedToLoginPageAsAnonymousUser() {
        //given
        String url = String.format("http://localhost:%s%s" + "/client/profile", port, basePath);
        String expectedPartOfHTML = "<form class=\"form-signin\" method=\"post\" action=\"/supperspeed/login\">";

        //when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String body = response.getBody();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(body).contains(expectedPartOfHTML);
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_FLYWAY_1, password = testPassword, authorities = "CLIENT")
    void checkThatYouCanGetUserProfile() throws Exception {
        //given, when
        ResultActions result = mockMvc.perform(get(CLIENT_PROFILE));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("clientDTO"))
                .andExpect(model().attributeExists("addressDTO"))
                .andExpect(model().attributeExists("clientId"))
                .andExpect(view().name("client_profile"));
    }

    @Test
    @WithMockUser(username = "fail", password = testPassword, authorities = "CLIENT")
    void checkThatExceptionIsThrownWhenNonExistingUserTriesToGetProfile() throws Exception {
        //given, when
        ResultActions result = mockMvc.perform(get(CLIENT_PROFILE));

        //then
        result
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("error"));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_FLYWAY_1, password = testPassword, authorities = "CLIENT")
    void checkThatYouCanUpdateClientProfile() throws Exception {
        //given
        int clientId = 1;
        String action = "updateData";
        String newName = "changed name";
        String newSurname = "changed surname";
        ClientEntity oldClient = clientJpaRepository.findById(clientId).orElseThrow(
                () -> new NotFoundException("Check Flyway migrations"));

        ClientDTO clientDTO = ClientDTO.builder()
                .id(clientId)
                .name(newName)
                .surname(newSurname)
                .build();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = clientDTO.asMap();
        parametersMap.putIfAbsent("action", action);
        parametersMap.putIfAbsent("clientId", Integer.toString(clientId));
        parametersMap.forEach(params::add);

        //when
        ResultActions result = mockMvc.perform(post(UPDATE_PROFILE)
                .params(params));

        ClientEntity newClient = clientJpaRepository.findById(clientId).orElseThrow(
                () -> new NotFoundException("Check Flyway migrations"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + CLIENT_PROFILE));

        assertThat(newClient.getName()).isEqualTo(newName);
        assertThat(newClient.getSurname()).isEqualTo(newSurname);
        assertNotEquals(newClient.getName(), oldClient.getName());
        assertNotEquals(newClient.getSurname(), oldClient.getSurname());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_FLYWAY_1, password = testPassword, authorities = "CLIENT")
    void checkThatYouCanUpdateUserAddress() throws Exception {
        //given
        int addressId = 4;
        int clientId = 1;
        String action = "updateAddress";
        String newStreetName = "newStreetName";
        String newCity = "newCity";
        String newCountry = "newCountry";
        String newPostalCode = "newPostalCode";
        String newBuildingNumber = "random";
        Integer newLocalNumber = 999;

        AddressEntity oldAddress = addressJpaRepository.findById(addressId).orElseThrow(
                () -> new NotFoundException("Check Flyway migrations"));

        AddressDTO addressDTO = AddressDTO.builder()
                .streetName(newStreetName)
                .city(newCity)
                .country(newCountry)
                .postalCode(newPostalCode)
                .buildingNumber(newBuildingNumber)
                .localNumber(newLocalNumber)
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = addressDTO.asMap();
        parametersMap.putIfAbsent("action", action);
        parametersMap.putIfAbsent("clientId", Integer.toString(clientId));
        parametersMap.forEach(params::add);

        //when
        ResultActions result = mockMvc.perform(post(UPDATE_PROFILE)
                .params(params));

        AddressEntity newAddress = addressJpaRepository.findById(addressId).orElseThrow(
                () -> new NotFoundException("Check Flyway migrations"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + CLIENT_PROFILE));

        assertThat(newAddress.getStreetName()).isEqualTo(newStreetName);
        assertThat(newAddress.getCity()).isEqualTo(newCity);
        assertThat(newAddress.getCountry()).isEqualTo(newCountry);
        assertThat(newAddress.getPostalCode()).isEqualTo(newPostalCode);
        assertThat(newAddress.getBuildingNumber()).isEqualTo(newBuildingNumber);
        assertThat(newAddress.getLocalNumber()).isEqualTo(newLocalNumber);
        assertNotEquals(newAddress.getStreetName(), oldAddress.getStreetName());
        assertNotEquals(newAddress.getCity(), oldAddress.getCity());
        assertNotEquals(newAddress.getCountry(), oldAddress.getCountry());
        assertNotEquals(newAddress.getPostalCode(), oldAddress.getPostalCode());
        assertNotEquals(newAddress.getBuildingNumber(), oldAddress.getBuildingNumber());
        assertNotEquals(newAddress.getLocalNumber(), oldAddress.getLocalNumber());
    }
}