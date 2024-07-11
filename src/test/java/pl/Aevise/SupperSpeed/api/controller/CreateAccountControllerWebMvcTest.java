package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.Aevise.SupperSpeed.api.controller.utils.PaginationAndSortingUtils;
import pl.Aevise.SupperSpeed.api.dto.CuisineDTO;
import pl.Aevise.SupperSpeed.business.ClientService;
import pl.Aevise.SupperSpeed.business.CuisineService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.business.UserService;
import pl.Aevise.SupperSpeed.infrastructure.security.RolesService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.CreateAccountController.CREATE_ACCOUNT_PAGE;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.cuisineDTO1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.cuisineDTO2;

@WebMvcTest(controllers = CreateAccountController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class CreateAccountControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private ClientService clientService;
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private RolesService rolesService;
    @MockBean
    private CuisineService cuisineService;

    @Test
    void getAccountCreationForm() throws Exception {
        //given
        var cuisines = List.of(
                cuisineDTO1(),
                cuisineDTO2()
        );

        //when
        when(cuisineService.findAllSorted(PaginationAndSortingUtils.ASC.getSortingDirection())).thenReturn(cuisines);

        ResultActions perform = mockMvc.perform(get(CREATE_ACCOUNT_PAGE)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("anonymousUser")));

        //then
        perform
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("cuisinesListDTO"))
                .andExpect(view().name("create_account_page"));
    }

    @Test
    void createClient() {
    }

    @Test
    void createRestaurant() {
    }
}