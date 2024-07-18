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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
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

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.CreateAccountController.*;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.supperUser4;

@WebMvcTest(controllers = CreateAccountController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
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

    public static Stream<Arguments> checkThatPhoneValidationWorksCorrectly() {
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

    public static Stream<Arguments> checkThatEmailVerificationWorksCorrectlyForCreatingRestaurantUser() {
        return Stream.of(
                Arguments.of(false, ""),
                Arguments.of(false, "testtestcom"),
                Arguments.of(false, "testtest.com"),
                Arguments.of(false, "test@test@com"),
                Arguments.of(true, "test@test.com"),
                Arguments.of(true, "test@test.com.pl")
        );
    }

    public static Stream<Arguments> checkThatEmailVerificationWorksCorrectlyForCreatingClientUser() {
        return Stream.of(
                Arguments.of(false, ""),
                Arguments.of(false, "testtestcom"),
                Arguments.of(false, "testtest.com"),
                Arguments.of(false, "test@test@com"),
                Arguments.of(true, "test@test.com"),
                Arguments.of(true, "test@test.com.pl")
        );
    }

    @Test
    void checkThatYouCanGetAccountCreationForm() throws Exception {
        //given
        var cuisines = List.of(
                cuisineDTO1(),
                cuisineDTO2()
        );

        //when
        when(cuisineService.findAllSorted(PaginationAndSortingUtils.ASC.getSortingDirection())).thenReturn(cuisines);

        ResultActions perform = mockMvc.perform(get(CREATE_ACCOUNT_PAGE)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cuisinesListDTO"))
                .andExpect(model().attribute("cuisinesListDTO", hasSize(cuisines.size())))
                .andExpect(model().attribute("cuisinesListDTO", containsInAnyOrder(cuisineDTO1(), cuisineDTO2())))
                .andExpect(view().name("create_account_page"));
    }

    @Test
    void checkThatYouCanNotCreateClientWhenUserWithGivenEmailAlreadyExists() throws Exception {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        SupperUser supperUser = supperUser4();
        ClientDTO clientDTO = clientDTO1();
        String role_id = "1";
        String password = "test";

        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> supperUserDTOMap = supperUserDTO.asMap();
        Map<String, String> parametersMap = clientDTO.asMap();
        parametersMap.putIfAbsent("role_id", role_id);
        parametersMap.putIfAbsent("password", password);
        parametersMap.forEach(parameters::add);
        supperUserDTOMap.forEach(parameters::add);

        //when
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(supperUser));

        ResultActions perform = mockMvc.perform(post(CREATE_ACCOUNT_USER).params(parameters)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        perform.andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + ACCOUNT_EXIST + "?email=" + supperUserDTO.getEmail()));
    }

    @Test
    void checkThatYouCanCreateClientAccount() throws Exception {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        ClientDTO clientDTO = clientDTO1();
        String role_id = "1";
        String password = "test";

        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> supperUserDTOMap = supperUserDTO.asMap();
        Map<String, String> parametersMap = clientDTO.asMap();
        parametersMap.putIfAbsent("role_id", role_id);
        parametersMap.putIfAbsent("password", password);
        parametersMap.forEach(parameters::add);
        supperUserDTOMap.forEach(parameters::add);

        //when
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(clientService.createClient(any(ClientEntity.class))).thenReturn(1);

        ResultActions perform = mockMvc.perform(post(CREATE_ACCOUNT_USER).params(parameters)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        perform.andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + CREATE_ACCOUNT_PAGE));
    }

    @Test
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    void checkThatYouCanNotCreateRestaurantUserIfEmailAlreadyExists() throws Exception {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        SupperUser supperUser = supperUser4();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        String role_id = "1";
        String password = "test";
        String cuisine = "test cuisine1";

        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> supperUserDTOMap = supperUserDTO.asMap();
        Map<String, String> parametersMap = restaurantDTO.asMap();
        parametersMap.putIfAbsent("role_id", role_id);
        parametersMap.putIfAbsent("password", password);
        parametersMap.putIfAbsent("cuisine", cuisine);
        parametersMap.forEach(parameters::add);
        supperUserDTOMap.forEach(parameters::add);

        //when
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(supperUser));

        ResultActions perform = mockMvc.perform(post(CREATE_ACCOUNT_RESTAURANT).params(parameters)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        perform.andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + ACCOUNT_EXIST + "?email=" + supperUserDTO.getEmail()));
    }

    @Test
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    void checkThatYouCanNotCreateRestaurantWhichClosesBeforeOpens() throws Exception {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        String role_id = "1";
        String password = "test";
        String cuisine = "test cuisine1";
        restaurantDTO.setCloseHour(LocalTime.MIDNIGHT);
        restaurantDTO.setOpenHour(LocalTime.NOON);

        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> supperUserDTOMap = supperUserDTO.asMap();
        Map<String, String> parametersMap = restaurantDTO.asMap();
        parametersMap.putIfAbsent("role_id", role_id);
        parametersMap.putIfAbsent("password", password);
        parametersMap.putIfAbsent("cuisine", cuisine);
        parametersMap.forEach(parameters::add);
        supperUserDTOMap.forEach(parameters::add);

        //when
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());

        ResultActions perform = mockMvc.perform(post(CREATE_ACCOUNT_RESTAURANT).params(parameters)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        perform.andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("error"));
    }

    @Test
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    void checkThatYouCanCorrectlyAddNewRestaurantUser() throws Exception {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        String role_id = "1";
        String password = "test";
        String cuisine = "test cuisine1";

        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> supperUserDTOMap = supperUserDTO.asMap();
        Map<String, String> parametersMap = restaurantDTO.asMap();
        parametersMap.putIfAbsent("role_id", role_id);
        parametersMap.putIfAbsent("password", password);
        parametersMap.putIfAbsent("cuisine", cuisine);
        parametersMap.forEach(parameters::add);
        supperUserDTOMap.forEach(parameters::add);

        //when
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(restaurantService.createRestaurant(any(RestaurantEntity.class))).thenReturn(restaurantDTO1().getRestaurantId());

        ResultActions perform = mockMvc.perform(post(CREATE_ACCOUNT_RESTAURANT).params(parameters)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        perform.andExpect(status().isFound())
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(view().name("redirect:" + CREATE_ACCOUNT_PAGE));
    }

    @ParameterizedTest
    @MethodSource
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    void checkThatPhoneValidationWorksCorrectly(Boolean correctPhone, String phone) throws Exception {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        String role_id = "1";
        String password = "test";
        String cuisine = "test cuisine1";
        restaurantDTO.setPhone(phone);

        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> supperUserDTOMap = supperUserDTO.asMap();
        Map<String, String> parametersMap = restaurantDTO.asMap();
        parametersMap.putIfAbsent("role_id", role_id);
        parametersMap.putIfAbsent("password", password);
        parametersMap.putIfAbsent("cuisine", cuisine);
        parametersMap.forEach(parameters::add);
        supperUserDTOMap.forEach(parameters::add);

        //when
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(restaurantService.createRestaurant(any(RestaurantEntity.class))).thenReturn(restaurantDTO1().getRestaurantId());

        //then
        if (correctPhone) {
            mockMvc.perform(post(CREATE_ACCOUNT_RESTAURANT).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isFound())
                    .andExpect(model().attributeDoesNotExist("errorMessage"))
                    .andExpect(view().name("redirect:" + CREATE_ACCOUNT_PAGE));
        } else {
            mockMvc.perform(post(CREATE_ACCOUNT_RESTAURANT).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isBadRequest())
                    .andExpect(model().attributeExists("errorMessage"))
                    .andExpect(model().attribute("errorMessage", Matchers.containsString(phone)));
        }
    }

    @ParameterizedTest
    @MethodSource
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    void checkThatEmailVerificationWorksCorrectlyForCreatingRestaurantUser(Boolean correctEmail, String email) throws Exception {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        String role_id = "1";
        String password = "test";
        String cuisine = "test cuisine1";
        supperUserDTO.setEmail(email);

        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> supperUserDTOMap = supperUserDTO.asMap();
        Map<String, String> parametersMap = restaurantDTO.asMap();
        parametersMap.putIfAbsent("role_id", role_id);
        parametersMap.putIfAbsent("password", password);
        parametersMap.putIfAbsent("cuisine", cuisine);
        parametersMap.forEach(parameters::add);
        supperUserDTOMap.forEach(parameters::add);

        //when
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(restaurantService.createRestaurant(any(RestaurantEntity.class))).thenReturn(restaurantDTO1().getRestaurantId());

        //then
        if (correctEmail) {
            mockMvc.perform(post(CREATE_ACCOUNT_RESTAURANT).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isFound())
                    .andExpect(model().attributeDoesNotExist("errorMessage"))
                    .andExpect(view().name("redirect:" + CREATE_ACCOUNT_PAGE));
        } else {
            mockMvc.perform(post(CREATE_ACCOUNT_RESTAURANT).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isBadRequest())
                    .andExpect(model().attributeExists("errorMessage"))
                    .andExpect(model().attribute("errorMessage", Matchers.containsString(email)));
        }
    }

    @ParameterizedTest
    @MethodSource
    void checkThatEmailVerificationWorksCorrectlyForCreatingClientUser(Boolean correctEmail, String email) throws Exception {
        //given
        SupperUserDTO supperUserDTO = supperUserDTO1();
        ClientDTO clientDTO = clientDTO1();
        String role_id = "1";
        String password = "test";
        supperUserDTO.setEmail(email);

        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> supperUserDTOMap = supperUserDTO.asMap();
        Map<String, String> parametersMap = clientDTO.asMap();
        parametersMap.putIfAbsent("role_id", role_id);
        parametersMap.putIfAbsent("password", password);
        parametersMap.forEach(parameters::add);
        supperUserDTOMap.forEach(parameters::add);

        //when
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(clientService.createClient(any(ClientEntity.class))).thenReturn(clientDTO.getId());

        //then
        if (correctEmail) {
            mockMvc.perform(post(CREATE_ACCOUNT_USER).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isFound())
                    .andExpect(model().attributeDoesNotExist("errorMessage"))
                    .andExpect(view().name("redirect:" + CREATE_ACCOUNT_PAGE));
        } else {
            mockMvc.perform(post(CREATE_ACCOUNT_USER).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isBadRequest())
                    .andExpect(model().attributeExists("errorMessage"))
                    .andExpect(model().attribute("errorMessage", Matchers.containsString(email)));
        }
    }
}