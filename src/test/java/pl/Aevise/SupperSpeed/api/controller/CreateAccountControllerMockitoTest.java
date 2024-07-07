package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import pl.Aevise.SupperSpeed.api.controller.utils.PaginationAndSortingUtils;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.ClientService;
import pl.Aevise.SupperSpeed.business.CuisineService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.business.UserService;
import pl.Aevise.SupperSpeed.domain.SupperUser;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.RolesService;
import pl.Aevise.SupperSpeed.infrastructure.security.dto.SupperUserDTO;
import pl.Aevise.SupperSpeed.util.POJOFixtures;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.clientEntity1;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.restaurantEntity1;

@ExtendWith(MockitoExtension.class)
class CreateAccountControllerMockitoTest {
    @Mock
    private UserService userService;
    @Mock
    private ClientService clientService;
    @Mock
    private RestaurantService restaurantService;
    @Mock
    private RolesService rolesService;
    @Mock
    private CuisineService cuisineService;

    @InjectMocks
    private CreateAccountController createAccountController;

    @Test
    void checkThatYouCanGetAccountCreationForm() {
        //given
        var cuisines = List.of(cuisineDTO1(), cuisineDTO2());
        String sortingDirection = PaginationAndSortingUtils.ASC.getSortingDirection();

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(cuisineService.findAllSorted(sortingDirection)).thenReturn(cuisines);

        String result = createAccountController.getAccountCreationForm(model);

        //then
        assertEquals(result, "create_account_page");
    }

    @Test
    void checkThatYouCannotCreateClientUserIfHeAlreadyExists() {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        SupperUser supperUser = POJOFixtures.supperUser4();
        ClientDTO clientDTO = clientDTO1();
        String roleId = "1";
        String password = "pass";

        //when
        when(userService.findUserByEmail(supperUserDTO.getEmail())).thenReturn(Optional.of(supperUser));

        String result = createAccountController.createClient(supperUserDTO, clientDTO, roleId, password);

        //then
        assertEquals(result, "/create/exist");
    }

    @Test
    void checkThatYouCanCreateClient() {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        ClientEntity clientEntity = clientEntity1();
        ClientDTO clientDTO = clientDTO1();
        String roleId = "1";
        String password = "pass";

        //when
        when(userService.findUserByEmail(supperUserDTO.getEmail())).thenReturn(Optional.empty());
        when(clientService.createClient(clientEntity)).thenReturn(1);

        String result = createAccountController.createClient(supperUserDTO, clientDTO, roleId, password);

        //then
        assertEquals(result, "redirect:/create");
    }

    @Test
    void checkThatYouCannotCreateRestaurantUserIfItExists() {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        SupperUser supperUser = POJOFixtures.supperUser4();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        String cuisine = cuisineDTO1().getCuisine();
        String roleId = "1";
        String password = "pass";

        //when
        when(userService.findUserByEmail(supperUserDTO.getEmail())).thenReturn(Optional.of(supperUser));

        String result = createAccountController.createRestaurant(supperUserDTO, restaurantDTO, roleId, password, cuisine);

        //then
        assertEquals(result, "/create/exist");
    }

    @Test
    void checkThatYouCannotCreateRestaurantUserIfItClosesBeforeOpens() {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        String cuisine = cuisineDTO1().getCuisine();
        String roleId = "1";
        String password = "pass";
        String expectedErrorMessage = "Time error open hour shouldn't be after closing hour";

        restaurantDTO.setCloseHour(LocalTime.MIDNIGHT);
        restaurantDTO.setOpenHour(LocalTime.NOON);

        //when
        when(userService.findUserByEmail(supperUserDTO.getEmail())).thenReturn(Optional.empty());

        RuntimeException expectedException = assertThrows(RuntimeException.class,
                () -> createAccountController.createRestaurant(supperUserDTO, restaurantDTO, roleId, password, cuisine));

        //then
        assertEquals(expectedErrorMessage, expectedException.getMessage());
    }

    @Test
    void checkThatYouCanCreateRestaurant() {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        RestaurantEntity restaurantEntity = restaurantEntity1();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        String cuisine = cuisineDTO1().getCuisine();
        String roleId = "1";
        String password = "pass";

        //when
        when(userService.findUserByEmail(supperUserDTO.getEmail())).thenReturn(Optional.empty());
        when(restaurantService.createRestaurant(restaurantEntity)).thenReturn(1);

        String result = createAccountController.createRestaurant(supperUserDTO, restaurantDTO, roleId, password, cuisine);

        //then
        assertEquals(result, "redirect:/create");
    }
}