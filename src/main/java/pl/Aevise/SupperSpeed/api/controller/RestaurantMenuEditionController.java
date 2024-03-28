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
import pl.Aevise.SupperSpeed.domain.Dish;
import pl.Aevise.SupperSpeed.domain.DishCategory;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.domain.SupperUser;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.RestaurantEntityMapper;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RestaurantMenuEditionController {

    static final String RESTAURANT_MENU_EDIT = "/restaurant/profile/menu";
    static final String RESTAURANT_MENU_UPDATE_DISH = "/restaurant/profile/menu/updateDish";
    static final String RESTAURANT_MENU_DELETE_DISH = "/restaurant/profile/menu/deleteDish";
    static final String RESTAURANT_MENU_UPDATE_CATEGORY = "/restaurant/profile/menu/updateCategory";
    static final String RESTAURANT_MENU_DELETE_CATEGORY = "/restaurant/profile/menu/deleteCategory";
    static final String RESTAURANT_MENU_ADD_CATEGORY = "/restaurant/profile/menu/addCategory";
    static final String RESTAURANT_MENU_ADD_DISH = "/restaurant/profile/menu/addDish";
    static final String ERROR = "error";

    private final DishListService dishListService;
    private final ProfileService profileService;
    private final DishCategoryService dishCategoryService;
    private final DishService dishService;
    private final RestaurantService restaurantService;


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
            model.addAttribute("categories", dishCategories);

            return "restaurant_menu_edit";
        }
        return ERROR;
    }

    @PostMapping(RESTAURANT_MENU_UPDATE_DISH)
    public String updateDish(
            @ModelAttribute DishDTO dishDTO
    ) {
        if (dishDTO.getAvailability() == null) {
            dishDTO.setAvailability(false);
        }
        dishService.updateDish(dishDTO);
        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

    @PostMapping(RESTAURANT_MENU_DELETE_DISH)
    public String deleteDish(
            @RequestParam(value = "dishId") Integer dishId
    ) {
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
    ) {
        dishCategoryService.deleteCategory(categoryId);
        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

    @PostMapping(RESTAURANT_MENU_ADD_CATEGORY)
    public String addCategory(
            @RequestParam(value = "categoryName") String categoryName,
            @RequestParam(value = "restaurantId") String restaurantId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        dishCategoryService.addCategory(dishCategoryService.buildDishCategory(restaurantId, categoryName));
        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

    @PostMapping(RESTAURANT_MENU_ADD_DISH)
    public String addDish(
            @ModelAttribute DishDTO dishDTO,
            @RequestParam(value = "restaurantId") Integer restaurantId,
            @RequestParam(value = "categoryId") Integer categoryId
    ) {
        Dish dish = buildDish(dishDTO, restaurantId, categoryId);
        dishService.addDish(dish);
        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

    private Dish buildDish(DishDTO dishDTO, Integer restaurantId, Integer categoryId) {
        return Dish.builder()
                .name(dishDTO.getName())
                .photo(dishDTO.getPhoto())
                .price(dishDTO.getPrice())
                .description(dishDTO.getDescription())
                .availability(Optional
                        .ofNullable(dishDTO.getAvailability())
                        .orElse(false))
                .restaurant(RestaurantEntity
                        .builder()
                        .id(restaurantId)
                        .build())
                .dishCategory(DishCategoryEntity
                        .builder()
                        .dishCategoryId(categoryId)
                        .build())
                .build();
    }

}
