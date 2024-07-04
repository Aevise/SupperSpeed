package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.ui.ExtendedModelMap;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.ClientMapper;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.ClientProfileService;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.domain.Client;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.testPassword;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.addressDTO4;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.clientDTO1;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.address4;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.client1;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
class ClientProfileControllerMockitoTest {

    @Mock
    private ClientProfileService clientProfileService;
    @Mock
    private AddressService addressService;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private AddressMapper addressMapper;


    @InjectMocks
    private ClientProfileController clientProfileController;


    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatRetrievingClientProfileWorksCorrectly() {
        //given
        UserDetails userDetails = User.withUsername(TEST_CLIENT_EMAIL_1).password(testPassword).authorities("CLIENT").build();

        Optional<Client> client1 = Optional.of(client1());
        Optional<Address> address = Optional.of(address4());

        when(clientProfileService.findClientByEmail(TEST_CLIENT_EMAIL_1)).thenReturn(client1);
        when(clientMapper.mapToDTO(client1.get())).thenReturn(clientDTO1());

        when(addressService.findById(client1.get().getAddress().getAddressId())).thenReturn(address);
        when(addressMapper.mapToDTO(address.get())).thenReturn(addressDTO4());

        //when
        ExtendedModelMap model = new ExtendedModelMap();
        String result = clientProfileController.getClientProfile(model, userDetails);

        //then
        assertThat(result).isEqualTo("client_profile");
        assertThat(model.getAttribute("clientDTO")).isEqualTo(clientDTO1());
        assertThat(model.getAttribute("addressDTO")).isEqualTo(addressDTO4());
        assertThat(model.getAttribute("clientId")).isEqualTo(clientDTO1().getId());
    }

    @Test
    void updateProfile() {
    }
}