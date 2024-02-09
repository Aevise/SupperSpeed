package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.DishCategoryMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.DishMapper;
import pl.Aevise.SupperSpeed.business.DishCategoryService;
import pl.Aevise.SupperSpeed.business.DishService;
import pl.Aevise.SupperSpeed.business.ProfileService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.domain.SupperUser;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RestaurantMenuEditionController {

    private final RestaurantService restaurantService;

    private final DishCategoryService dishCategoryService;
    private final DishCategoryMapper dishCategoryMapper;

    private final DishService dishService;
    private final DishMapper dishMapper;

    private final ProfileService profileService;

    static final String RESTAURANT_MENU_EDIT = "/restaurant/profile/menu";

    @GetMapping(RESTAURANT_MENU_EDIT)
    public String getRestaurantMenuEdit(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<SupperUser> user = profileService.findUserByEmail(userDetails.getUsername());

        if (user.isPresent()) {
            Integer restaurantId = user.get().getSupperUserId();

            List<DishCategoryDTO> dishCategoryDTO = dishCategoryService
                    .findAllByRestaurant(restaurantId)
                    .stream()
                    .map(dishCategoryMapper::mapToDTO)
                    .toList();

//            List<DishDTO> dishesDTO = dishService
//                    .findAllByRestaurant(restaurantId)
//                    .stream()
//                    .map(dishMapper::mapToDTO)
//                    .toList();

            model.addAttribute("dishCategoryDTO", dishCategoryDTO);
//            model.addAttribute("dishesDTO", dishesDTO);
        }


        return "restaurant_menu_edit";
    }
}
