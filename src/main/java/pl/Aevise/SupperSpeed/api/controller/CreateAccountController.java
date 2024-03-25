package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.business.ClientService;
import pl.Aevise.SupperSpeed.business.UserService;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.dto.SupperUserDTO;

import java.time.OffsetDateTime;

@Controller
@AllArgsConstructor
public class CreateAccountController {

    private final UserService userService;
    private final ClientService clientService;

    private static final String CREATE_ACCOUNT_PAGE = "/create";
    private static final String CREATE_ACCOUNT_USER = "/create/user";
    private static final String CREATE_ACCOUNT_RESTAURANT = "/create/restaurant";
    private static final String ACCOUNT_EXIST = "/create/exist";

    @GetMapping(CREATE_ACCOUNT_PAGE)
    String getAccountCreationForm() {
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

        int clientId = clientService.createClient(
                createClientEntity(
                        supperUserDTO,
                        clientDTO,
                        role_id
                        , password
                )
        );


//        userService.createUser();
//        clientService.createClient();


        return "redirect:" + CREATE_ACCOUNT_PAGE;
    }

    @PostMapping(CREATE_ACCOUNT_RESTAURANT)
    String createRestaurant() {

        return "redirect:" + CREATE_ACCOUNT_PAGE;
    }

    boolean checkIfUserExist(String email) {
        return userService.findUserByEmail(email).isPresent();
    }

    ClientEntity createClientEntity(
            SupperUserDTO supperUserDTO,
            ClientDTO clientDTO,
            String role,
            String password) {
        final boolean userDefaultActive = false;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return ClientEntity.builder()
                .supperUser(
                        SupperUserEntity.builder()
                                .email(supperUserDTO.getEmail())
                                .password(encoder.encode(password))
                                .active(userDefaultActive)
                                .creationDateTime(OffsetDateTime.now())
                                .lastLoginDateTime(OffsetDateTime.now())
                                .build()
                )
                .name(clientDTO.getName())
                .surname(clientDTO.getSurname())
                .phone(clientDTO.getPhone())
                .address(new AddressEntity())
                .build();
    }

    RestaurantEntity createRestaurantEntity(){
        return null;
    }
}
