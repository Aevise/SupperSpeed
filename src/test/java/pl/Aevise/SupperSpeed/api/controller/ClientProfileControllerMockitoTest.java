package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.ClientProfileService;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.ClientEntityMapper;
import pl.Aevise.SupperSpeed.util.EntityFixtures;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientProfileControllerMockitoTest {

    @Mock
    private ClientProfileService clientProfileService;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private ClientProfileController clientProfileController;

    private ClientEntityMapper clientEntityMapper;


    @Test
    @WithMockUser("testUser1")
    void checkThatRetrievingClientProfileWorksCorrectly() {
        //given
        String clientEmail = "test4@gmail.com";
        ClientEntity clientEntity = EntityFixtures.clientEntity1();
        Client client = clientEntityMapper.mapFromEntity(clientEntity);

        ExtendedModelMap model = new ExtendedModelMap();

        when(clientProfileService.findClientByEmail(clientEmail)).thenReturn(Optional.of(client));

        //when
        String result = clientProfileController.getClientProfile(model, );

    }

    @Test
    void updateProfile() {
    }
}