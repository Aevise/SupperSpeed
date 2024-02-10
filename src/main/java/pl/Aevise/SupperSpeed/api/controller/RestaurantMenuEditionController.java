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
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.business.DishListService;
import pl.Aevise.SupperSpeed.business.DishService;
import pl.Aevise.SupperSpeed.business.ProfileService;
import pl.Aevise.SupperSpeed.domain.SupperUser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RestaurantMenuEditionController {

    private final DishListService dishListService;
    private final ProfileService profileService;

    private final DishService dishService;


    static final String RESTAURANT_MENU_EDIT = "/restaurant/profile/menu";
    static final String RESTAURANT_MENU_UPDATE_DISH = "/restaurant/profile/menu/updateDish";
    static final String RESTAURANT_MENU_DELETE_DISH = "/restaurant/profile/menu/deleteDish";

    @GetMapping(RESTAURANT_MENU_EDIT)
    public String getRestaurantMenuEdit(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<SupperUser> user = profileService.findUserByEmail(userDetails.getUsername());
        if (user.isPresent()) {
            Integer restaurantId = user.get().getSupperUserId();
            HashMap<String, List<DishDTO>> dishListByCategory = dishListService.getDishListByCategoryFromRestaurant(restaurantId);
            model.addAttribute("dishesByCategory", dishListByCategory);
        }
        return "restaurant_menu_edit";
    }

    @PostMapping(RESTAURANT_MENU_UPDATE_DISH)
    public String updateDish(
            @ModelAttribute DishDTO dishDTO
    ) {
        dishService.updateDish(dishDTO);
        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

    @PostMapping(RESTAURANT_MENU_DELETE_DISH)
    public String deleteDish(
            @RequestParam(value = "dishId") Integer dishId
    ){
        dishService.deleteDish(dishId);
        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

}
