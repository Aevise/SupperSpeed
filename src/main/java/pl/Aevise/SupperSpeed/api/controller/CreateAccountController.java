package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    //TODO zmienić potem na false i dodac aktywowanie uzytkownika za pomoca maila
    static final boolean userDefaultActive = true;
    static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final String CREATE_ACCOUNT_PAGE = "/create";
    private static final String CREATE_ACCOUNT_USER = "/create/user";
    private static final String CREATE_ACCOUNT_RESTAURANT = "/create/restaurant";
    private static final String ACCOUNT_EXIST = "/create/exist";
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
            @ModelAttribute SupperUserDTO supperUserDTO,
            @ModelAttribute ClientDTO clientDTO,
            @RequestParam("role_id") String role_id,
            @RequestParam("password") String password
    ) {
        if (checkIfUserExist(supperUserDTO.getEmail())) {
            return ACCOUNT_EXIST;
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
            @ModelAttribute SupperUserDTO supperUserDTO,
            @ModelAttribute RestaurantDTO restaurantDTO,
            @RequestParam("role_id") String role_id,
            @RequestParam("password") String password,
            @RequestParam("cuisine") String cuisine
    ) {
        if (checkIfUserExist(supperUserDTO.getEmail())) {
            return ACCOUNT_EXIST;
        }

        if (restaurantDTO.getOpenHour().isAfter(restaurantDTO.getCloseHour())) {
            throw new RuntimeException("Time error open hour shouldn't be after closing hour");
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

    ClientEntity createClientEntity(
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

    RestaurantEntity createRestaurantEntity(
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

    boolean checkIfUserExist(String email) {
        return userService.findUserByEmail(email).isPresent();
    }

    RolesEntity getRoleById(Integer roleId) {
        return rolesService
                .findById(roleId)
                .orElse(null);
    }
}
