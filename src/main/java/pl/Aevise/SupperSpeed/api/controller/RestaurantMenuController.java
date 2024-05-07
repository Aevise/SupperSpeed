package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.business.DishListService;
import pl.Aevise.SupperSpeed.business.DishService;
import pl.Aevise.SupperSpeed.business.ImageHandlingService;

import java.util.List;

import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.MAX_LOGO_HEIGHT;
import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.MAX_LOGO_WIDTH;

@Controller
@AllArgsConstructor
public class RestaurantMenuController {

    static final String RESTAURANT_MENU = "/menu";
    static final String ERROR = "error";

    private final DishService dishService;
    private final DishListService dishListService;
    private final ImageHandlingService imageHandlingService;

    @GetMapping(RESTAURANT_MENU)
    public String getRestaurantMenu
            (
                    @RequestParam Integer restaurantId,
                    @RequestParam String restaurantName,
                    Model model
            ) {
        if (restaurantId <= 0) {
            return ERROR;
        }
        List<DishCategoryDTO> dishCategories = dishListService.getDishCategoriesByRestaurantId(restaurantId);

        if (dishCategories.isEmpty()) {
            return ERROR;
        }

        var dishMap = dishListService.extractDishesByCategory(dishCategories, true);

        model.addAttribute("dishesByCategory", dishMap);
        model.addAttribute("restaurantId", restaurantId);

        model.addAttribute("imageWidth", MAX_LOGO_WIDTH);
        model.addAttribute("imageHeight", MAX_LOGO_HEIGHT);

        String restaurantDirectory = imageHandlingService.getRestaurantName(restaurantId, restaurantName);
        model.addAttribute("restaurantDirectory", restaurantDirectory);

        return "restaurant_menu";
    }
}