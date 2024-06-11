package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.RestaurantMapper;
import pl.Aevise.SupperSpeed.business.DishListService;
import pl.Aevise.SupperSpeed.business.DishService;
import pl.Aevise.SupperSpeed.business.ImageHandlingService;
import pl.Aevise.SupperSpeed.business.RestaurantService;

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

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    @GetMapping(RESTAURANT_MENU)
    public String getRestaurantMenu
            (
                    @RequestParam Integer restaurantId,
                    Model model
            ) {
        if (restaurantId <= 0) {
            return ERROR;
        }
        List<DishCategoryDTO> dishCategories = dishListService.getDishCategoriesByRestaurantId(restaurantId);

        var dishMap = dishService.extractDishesByCategory(dishCategories, true);
        RestaurantDTO restaurantDTO = restaurantMapper.mapToDTO(restaurantService.findRestaurantById(restaurantId));
        String restaurantDirectory = imageHandlingService.getRestaurantName(restaurantId, restaurantDTO.getRestaurantName());

        model.addAttribute("dishesByCategory", dishMap);
        model.addAttribute("restaurantId", restaurantId);

        model.addAttribute("imageWidth", MAX_LOGO_WIDTH);
        model.addAttribute("imageHeight", MAX_LOGO_HEIGHT);
        model.addAttribute("restaurantDirectory", restaurantDirectory);

        return "restaurant_menu";
    }
}