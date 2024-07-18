package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.Aevise.SupperSpeed.api.controller.exception.UserNotFoundException;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.RestaurantMapper;
import pl.Aevise.SupperSpeed.business.DishListService;
import pl.Aevise.SupperSpeed.business.DishService;
import pl.Aevise.SupperSpeed.business.ImageHandlingService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;

import java.util.List;

import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.MAX_LOGO_HEIGHT;
import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.MAX_LOGO_WIDTH;

@Controller
@AllArgsConstructor
public class RestaurantMenuController {

    static final String RESTAURANT_MENU = "/menu/{restaurantId}/{restaurantName}";

    private final DishService dishService;
    private final DishListService dishListService;
    private final ImageHandlingService imageHandlingService;

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    private final SecurityService securityService;

    @GetMapping(RESTAURANT_MENU)
    public String getRestaurantMenu
            (
                    @PathVariable("restaurantId") Integer restaurantId,
                    @PathVariable("restaurantName") String restaurantName,
                    Model model
            ) {
        if (restaurantId <= 0) {
            throw new UserNotFoundException("Wrong identification provided. User Not Found.");
        }
        List<DishCategoryDTO> dishCategories = dishListService.getDishCategoriesByRestaurantId(restaurantId);

        var dishMap = dishService.extractDishesByCategory(dishCategories, true);
        RestaurantDTO restaurantDTO = restaurantMapper.mapToDTO(restaurantService.findRestaurantById(restaurantId));
        String restaurantDirectory = imageHandlingService.getRestaurantName(restaurantId, restaurantDTO.getRestaurantName());

        AddressDTO addressDTO = restaurantDTO.getAddress();
        String userRole = securityService.getUserAuthority();

        model.addAttribute("dishesByCategory", dishMap);
        model.addAttribute("restaurantId", restaurantId);

        model.addAttribute("imageWidth", MAX_LOGO_WIDTH);
        model.addAttribute("imageHeight", MAX_LOGO_HEIGHT);
        model.addAttribute("restaurantDirectory", restaurantDirectory);
        model.addAttribute("restaurantName", restaurantName);
        model.addAttribute("addressDTO", addressDTO);
        model.addAttribute("userRole", userRole);
        return "restaurant_menu";
    }
}