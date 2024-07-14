package pl.Aevise.SupperSpeed.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.controller.exception.IncorrectOpeningHourException;
import pl.Aevise.SupperSpeed.api.controller.utils.PaginationAndSortingUtils;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.api.dto.CuisineDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.ClientService;
import pl.Aevise.SupperSpeed.business.CuisineService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.business.UserService;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.RolesService;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.RolesEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.dto.SupperUserDTO;

import java.time.OffsetDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
public class CreateAccountController {

    public static final String CREATE_ACCOUNT_PAGE = "/create";
    public static final String CREATE_ACCOUNT_USER = "/create/user";
    public static final String CREATE_ACCOUNT_RESTAURANT = "/create/restaurant";
    public static final String ACCOUNT_EXIST = "/create/exist";
    //TODO zmienić potem na false i dodac aktywowanie uzytkownika za pomoca maila
    static final boolean userDefaultActive = true;
    static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserService userService;
    private final ClientService clientService;
    private final RestaurantService restaurantService;
    private final RolesService rolesService;
    private final CuisineService cuisineService;

    @GetMapping(CREATE_ACCOUNT_PAGE)
    String getAccountCreationForm(
            Model model
    ) {
        List<CuisineDTO> cuisines = cuisineService.findAllSorted(PaginationAndSortingUtils.ASC.getSortingDirection());
        model.addAttribute("cuisinesListDTO", cuisines);

        return "create_account_page";
    }

    @PostMapping(CREATE_ACCOUNT_USER)
    String createClient(
            @Valid @ModelAttribute SupperUserDTO supperUserDTO,
            @Valid @ModelAttribute ClientDTO clientDTO,
            @RequestParam("role_id") String role_id,
            @RequestParam("password") String password
    ) {
        if (checkIfUserExist(supperUserDTO.getEmail())) {
            return "redirect:" + ACCOUNT_EXIST + "?email=" + supperUserDTO.getEmail();
        }

        clientService.createClient(
                createClientEntity(
                        supperUserDTO,
                        clientDTO,
                        role_id,
                        password
                )
        );

        return "redirect:" + CREATE_ACCOUNT_PAGE;
    }

    @PostMapping(CREATE_ACCOUNT_RESTAURANT)
    String createRestaurant(
            @Valid @ModelAttribute SupperUserDTO supperUserDTO,
            @Valid @ModelAttribute RestaurantDTO restaurantDTO,
            @RequestParam("role_id") String role_id,
            @RequestParam("password") String password,
            @RequestParam("cuisine") String cuisine
    ) {
        if (checkIfUserExist(supperUserDTO.getEmail())) {
            return "redirect:" + ACCOUNT_EXIST + "?email=" + supperUserDTO.getEmail();

        }

        if (restaurantDTO.getOpenHour().isAfter(restaurantDTO.getCloseHour())) {
            throw new IncorrectOpeningHourException("Open hour shouldn't be after closing hour");
        }


        restaurantService.createRestaurant(
                createRestaurantEntity(
                        supperUserDTO,
                        restaurantDTO,
                        role_id,
                        password,
                        cuisine
                )
        );

        return "redirect:" + CREATE_ACCOUNT_PAGE;
    }

    @GetMapping(ACCOUNT_EXIST)
    String getAccountExistPage(
            Model model,
            @RequestParam String email
    ) {
        model.addAttribute("email", email);
        return "account_exist";
    }

    private ClientEntity createClientEntity(
            SupperUserDTO supperUserDTO,
            ClientDTO clientDTO,
            String role,
            String password) {
        return ClientEntity.builder()
                .supperUser(
                        SupperUserEntity.builder()
                                .email(supperUserDTO.getEmail())
                                .password(encoder.encode(password))
                                .active(userDefaultActive)
                                .creationDateTime(OffsetDateTime.now())
                                .lastLoginDateTime(OffsetDateTime.now())
                                .role(getRoleById(Integer.valueOf(role)))
                                .build()
                )
                .name(clientDTO.getName())
                .surname(clientDTO.getSurname())
                .phone(clientDTO.getPhone())
                .address(new AddressEntity())
                .build();
    }

    private RestaurantEntity createRestaurantEntity(
            SupperUserDTO supperUserDTO,
            RestaurantDTO restaurantDTO,
            String role,
            String password,
            String cuisine
    ) {
        return RestaurantEntity.builder()
                .supperUser(
                        SupperUserEntity.builder()
                                .email(supperUserDTO.getEmail())
                                .password(encoder.encode(password))
                                .active(userDefaultActive)
                                .creationDateTime(OffsetDateTime.now())
                                .lastLoginDateTime(OffsetDateTime.now())
                                .role(getRoleById(Integer.valueOf(role)))
                                .build())
                .cuisine(cuisineService.getCuisineByName(cuisine))
                .restaurantName(restaurantDTO.getRestaurantName())
                .phone(restaurantDTO.getPhone())
                .openHour(restaurantDTO.getOpenHour())
                .closeHour(restaurantDTO.getCloseHour())
                .address(new AddressEntity())
                .isShown(false)
                .build();
    }

    private boolean checkIfUserExist(String email) {
        return userService.findUserByEmail(email).isPresent();
    }

    private RolesEntity getRoleById(Integer roleId) {
        return rolesService
                .findById(roleId)
                .orElse(null);
    }
}
