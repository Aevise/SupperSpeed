package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.RestaurantMapper;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.ImageHandlingService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.domain.Restaurant;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RestaurantProfileController {

    static final String RESTAURANT_PROFILE = "/restaurant/profile";
    static final String RESTAURANT_UPDATE = "/restaurant/profile/update";
    static final String UPDATE_LOGO = "/restaurant/profile/update/logo";

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    private final ImageHandlingService imageHandlingService;

    @GetMapping(value = RESTAURANT_PROFILE)
    public String getRestaurantProfile(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        Optional<Restaurant> restaurant = restaurantService
                .findRestaurantByEmail(
                        userDetails.getUsername());

        if (restaurant.isPresent()) {
            RestaurantDTO restaurantDTO = restaurant.map(restaurantMapper::mapToDTO)
                    .orElseThrow(() -> new NoSuchElementException(
                            "Restaurant not found. Could not map restaurant to restaurantDTO"
                    ));
            Integer addressId = restaurant.get()
                    .getAddress()
                    .getAddressId();
            AddressDTO addressDTO = addressService.findById(addressId)
                    .map(addressMapper::mapToDTO)
                    .orElseThrow(() -> new NoSuchElementException(
                            "Could not find restaurant address with id: [%s]"
                                    .formatted(addressId)
                    ));

            Integer userId = restaurantDTO.getRestaurantId();
            model.addAttribute("restaurantDTO", restaurantDTO);
            model.addAttribute("addressDTO", addressDTO);
            model.addAttribute("userId", userId);
        }

        return "restaurant_profile";
    }

    @PostMapping(RESTAURANT_UPDATE)
    public String updateProfileInformation(
            @ModelAttribute RestaurantDTO restaurantDTO,
            @ModelAttribute AddressDTO addressDTO,
            @RequestParam(required = false) String action,
            @RequestParam String userId
    ) {
        if ("updateAddress".equals(action)) {
            restaurantService.updateAddress(addressDTO, Integer.valueOf(userId));
        } else {
            restaurantService.updateRestaurantInformation(restaurantDTO, Integer.valueOf(userId));
        }
        return "redirect:" + RESTAURANT_PROFILE;
    }

    @PostMapping(UPDATE_LOGO)
    public String updateRestaurantLogo(
            @RequestParam("image")MultipartFile image,
            @RequestParam("userId") Integer userId,
            @RequestParam("restaurantName") String restaurantName
            ) throws IOException {

        imageHandlingService.updateImage(image.getBytes(), userId, restaurantName);

        return "redirect:" + RESTAURANT_PROFILE;
    }
}
