package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.ClientMapper;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.ClientProfileService;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.domain.Client;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.ClientProfileController.CLIENT_PROFILE;
import static pl.Aevise.SupperSpeed.api.controller.ClientProfileController.UPDATE_PROFILE;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.addressDTO1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.clientDTO1;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.address1;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.client1;

@WebMvcTest(controllers = ClientProfileController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class ClientProfileControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private ClientProfileService clientProfileService;
    @MockBean
    private AddressService addressService;
    @MockBean
    private ClientMapper clientMapper;
    @MockBean
    private AddressMapper addressMapper;

    public static Stream<Arguments> phoneValidationShouldWorkCorrectly() {
        return Stream.of(
                Arguments.of(false, ""),
                Arguments.of(false, "+48 504 203 260@@"),
                Arguments.of(false, "+48.504.203.260"),
                Arguments.of(false, "+55(123) 456-78-90-"),
                Arguments.of(false, "+55(123) - 456-78-90"),
                Arguments.of(false, "504.203.260"),
                Arguments.of(false, " "),
                Arguments.of(false, "-"),
                Arguments.of(false, "()"),
                Arguments.of(false, "() + ()"),
                Arguments.of(false, "(21 7777"),
                Arguments.of(false, "+48 (21)"),
                Arguments.of(false, "+"),
                Arguments.of(false, " 1"),
                Arguments.of(false, "1"),
                Arguments.of(false, "+48 (12) 504 203 260"),
                Arguments.of(false, "+48 (12) 504-203-260"),
                Arguments.of(false, "+48(12)504203260"),
                Arguments.of(false, "555-5555-555"),
                Arguments.of(true, "+48 504 203 260")
        );
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatYouCanGetClientProfile() throws Exception {
        //given
        Client client = client1();
        ClientDTO clientDTO = clientDTO1();
        Address address = address1();
        AddressDTO addressDTO = addressDTO1();

        //when
        when(clientProfileService.findClientByEmail(TEST_CLIENT_EMAIL_1)).thenReturn(Optional.of(client));
        when(clientMapper.mapToDTO(client)).thenReturn(clientDTO);
        when(addressService.findById(clientDTO.getId())).thenReturn(Optional.of(address));
        when(addressMapper.mapToDTO(address)).thenReturn(addressDTO);

        //then
        mockMvc.perform(get(CLIENT_PROFILE).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(model().attributeExists("clientDTO"))
                .andExpect(model().attributeExists("addressDTO"))
                .andExpect(model().attributeExists("clientId"))
                .andExpect(view().name("client_profile"));

    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatYouCannotGetUserProfileIfItDoesNotExist() throws Exception {
        //given, when
        when(clientProfileService.findClientByEmail(TEST_CLIENT_EMAIL_1)).thenReturn(Optional.empty());

        //then
        mockMvc.perform(get(CLIENT_PROFILE).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("error"));

    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatYouCanUpdateClientInformation() throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = clientDTO1().asMap();
        parametersMap.putIfAbsent("clientId", clientDTO1().getId().toString());
        parametersMap.putIfAbsent("action", "updateClient");
        parametersMap.forEach(parameters::add);

        //when, then
        mockMvc.perform(post(UPDATE_PROFILE).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isFound())
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(view().name("redirect:" + CLIENT_PROFILE));

    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatYouCanUpdateClientAddressInformation() throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = addressDTO1().asMap();
        parametersMap.putIfAbsent("clientId", clientDTO1().getId().toString());
        parametersMap.putIfAbsent("action", "updateAddress");
        parametersMap.forEach(parameters::add);

        //when, then
        mockMvc.perform(post(UPDATE_PROFILE).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isFound())
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(view().name("redirect:" + CLIENT_PROFILE));
    }

    @Test
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    void shouldNotAllowAnyActionForNonClientUser() throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = addressDTO1().asMap();
        parametersMap.putIfAbsent("clientId", clientDTO1().getId().toString());
        parametersMap.putIfAbsent("action", "updateAddress");
        parametersMap.forEach(parameters::add);

        //when, then
        mockMvc.perform(post(UPDATE_PROFILE).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    @WithAnonymousUser
    void shouldNotAllowAnyActionForAnonymousUser() throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = addressDTO1().asMap();
        parametersMap.putIfAbsent("clientId", clientDTO1().getId().toString());
        parametersMap.putIfAbsent("action", "updateAddress");
        parametersMap.forEach(parameters::add);

        //when, then
        mockMvc.perform(post(UPDATE_PROFILE).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void checkThatYouGetErrorWhenTryToUpdateProfileWithoutAnyAuthorities() throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = addressDTO1().asMap();
        parametersMap.putIfAbsent("clientId", clientDTO1().getId().toString());
        parametersMap.putIfAbsent("action", "updateAddress");
        parametersMap.forEach(parameters::add);

        //when, then
        ResultActions perform = mockMvc.perform(post(UPDATE_PROFILE)
                .params(parameters)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(TEST_CLIENT_EMAIL_1).authorities()));

        perform
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("error"));
    }

    @ParameterizedTest
    @MethodSource
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void phoneValidationShouldWorkCorrectly(Boolean correctPhone, String phone) throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = clientDTO1().asMap();
        parametersMap.putIfAbsent("clientId", clientDTO1().getId().toString());
        parametersMap.putIfAbsent("action", "updateClient");
        parametersMap.put("phone", phone);
        parametersMap.forEach(parameters::add);

        // when, then
        if (correctPhone) {
            mockMvc.perform(post(UPDATE_PROFILE).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isFound())
                    .andExpect(model().attributeDoesNotExist("errorMessage"))
                    .andExpect(view().name("redirect:" + CLIENT_PROFILE))
                    .andExpect(redirectedUrl(CLIENT_PROFILE));
        } else {
            mockMvc.perform(post(UPDATE_PROFILE).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isBadRequest())
                    .andExpect(model().attributeExists("errorMessage"))
                    .andExpect(model().attribute("errorMessage", Matchers.containsString(phone)));
        }
    }


}