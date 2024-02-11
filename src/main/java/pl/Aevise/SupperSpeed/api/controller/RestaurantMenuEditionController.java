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
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.business.*;
import pl.Aevise.SupperSpeed.domain.SupperUser;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RestaurantMenuEditionController {

    private final DishListService dishListService;
    private final ProfileService profileService;
    private final DishCategoryService dishCategoryService;
    private final RestaurantService restaurantService;

    private final DishService dishService;


    static final String RESTAURANT_MENU_EDIT = "/restaurant/profile/menu";
    static final String RESTAURANT_MENU_UPDATE_DISH = "/restaurant/profile/menu/updateDish";
    static final String RESTAURANT_MENU_DELETE_DISH = "/restaurant/profile/menu/deleteDish";
    static final String RESTAURANT_MENU_UPDATE_CATEGORY = "/restaurant/profile/menu/updateCategory";
    static final String RESTAURANT_MENU_DELETE_CATEGORY = "/restaurant/profile/menu/deleteCategory";
    static final String RESTAURANT_MENU_ADD_CATEGORY = "/restaurant/profile/menu/addCategory";
    static final String RESTAURANT_MENU_ADD_DISH = "/restaurant/profile/menu/addDish";
    @GetMapping(RESTAURANT_MENU_EDIT)
    public String getRestaurantMenuEdit(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<SupperUser> user = profileService.findUserByEmail(userDetails.getUsername());
        if (user.isPresent()) {
            Integer restaurantId = user.get().getSupperUserId();
            List<DishCategoryDTO> dishCategories = dishListService.getDishCategoriesByRestaurantId(restaurantId);

            var dishList = dishListService.extractDishesByCategory(dishCategories);

            model.addAttribute("dishesByCategory", dishList);
            model.addAttribute("userId", restaurantId);

            return "restaurant_menu_edit";
        }
        return "error";
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

    @PostMapping(RESTAURANT_MENU_UPDATE_CATEGORY)
    public String updateCategory(
            @ModelAttribute DishCategoryDTO dishCategoryDTO
    ) {
        dishCategoryService.updateCategory(dishCategoryDTO);

        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

    @PostMapping(RESTAURANT_MENU_DELETE_CATEGORY)
    public String deleteCategory(
            @RequestParam(value = "dishCategoryId") Integer categoryId
    ){
        dishCategoryService.deleteCategory(categoryId);
        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

    @PostMapping(RESTAURANT_MENU_ADD_CATEGORY)
    public String addCategory(
            @RequestParam(value = "restaurantId") Integer restaurantId,
            @RequestParam(value = "categoryName") String categoryName
    ){
        dishCategoryService.addCategory(restaurantId, categoryName);
        return "redirect:" + RESTAURANT_MENU_EDIT;
    }
}
