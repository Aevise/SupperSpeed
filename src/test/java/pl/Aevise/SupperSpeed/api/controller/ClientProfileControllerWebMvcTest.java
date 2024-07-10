package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.ModelResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.ClientMapper;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.ClientProfileService;
import pl.Aevise.SupperSpeed.util.DTOFixtures;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.ClientProfileController.CLIENT_PROFILE;
import static pl.Aevise.SupperSpeed.api.controller.ClientProfileController.UPDATE_PROFILE;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.testPassword;

@WebMvcTest(controllers = ClientProfileController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
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


    @Test
    void getClientProfile() {
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, roles = "CLIENT")
    void checkThatYouCanUpdateClientInformation() throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = DTOFixtures.clientDTO1().asMap();
        parametersMap.putIfAbsent("clientId", DTOFixtures.clientDTO1().getId().toString());
        parametersMap.putIfAbsent("action", "updateClient");
        parametersMap.forEach(parameters::add);

        //when, then
        mockMvc.perform(post(UPDATE_PROFILE).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isFound())
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(view().name("redirect:" + CLIENT_PROFILE));

    }
}