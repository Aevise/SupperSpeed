package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.business.DishListService;
import pl.Aevise.SupperSpeed.business.DishService;

import java.util.List;

@Controller
@AllArgsConstructor
public class RestaurantMenuController {

    static final String RESTAURANT_MENU = "/menu";
    static final String ERROR = "error";

    private final DishService dishService;
    private final DishListService dishListService;

    @GetMapping(RESTAURANT_MENU)
    public String getRestaurantMenu
            (
                    @RequestParam(required = true) Integer restaurantId,
                    Model model
            ) {
        if (restaurantId <= 0) {
            return ERROR;
        }
        List<DishCategoryDTO> dishCategories = dishListService.getDishCategoriesByRestaurantId(restaurantId);

        if (dishCategories.isEmpty()) {
            return ERROR;
        }

        var dishMap = dishListService.extractDishesByCategory(dishCategories);
        model.addAttribute("dishesByCategory", dishMap);

        return "restaurant_menu";
    }
}