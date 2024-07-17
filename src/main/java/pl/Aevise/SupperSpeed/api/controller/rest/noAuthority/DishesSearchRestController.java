package pl.Aevise.SupperSpeed.api.controller.rest.noAuthority;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.Aevise.SupperSpeed.api.controller.utils.URLConstants;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.business.DishService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(URLConstants.API_UNAUTH)
@AllArgsConstructor
@Tag(name = "Dishes Search Controller", description = "Endpoints for searching dishes")
public class DishesSearchRestController {

    public static final String ALL_DISHES_FROM_RESTAURANT = "/dishes/{restaurantName}/category/all";
    public static final String ALL_DISHES_FROM_RESTAURANT_BY_CATEGORY = "/dishes/{restaurantName}/category/{category}";

    private final DishService dishService;

    @GetMapping(value = ALL_DISHES_FROM_RESTAURANT)
    @Operation(summary = "Get all dishes from a restaurant")
    public ResponseEntity<List<DishDTO>> allDishesFromRestaurant(
            @Parameter(description = "Name of the restaurant", required = true)
            @PathVariable String restaurantName
    ) {
        if (Objects.isNull(restaurantName)) {
            return ResponseEntity.notFound().build();
        }

        List<DishDTO> dishes = dishService.findAllNotHiddenDishes(restaurantName)
                .stream()
                .sorted(Comparator.comparing(DishDTO::getDishId))
                .peek(dish -> dish.setDishId(null))
                .toList();
        if (dishes.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(dishes);
    }

    @GetMapping(value = ALL_DISHES_FROM_RESTAURANT_BY_CATEGORY)
    @Operation(summary = "Get dishes from a restaurant by category")
    public ResponseEntity<List<DishDTO>> dishesFromRestaurantByCategory(
            @Parameter(description = "Name of the restaurant", required = true)
            @PathVariable String restaurantName,
            @Parameter(description = "Category of the dishes", required = true)
            @PathVariable String category
    ) {
        if (Objects.isNull(restaurantName) || Objects.isNull(category)) {
            return ResponseEntity.notFound().build();
        }
        List<DishDTO> dishes = dishService.findNotHiddenDishesByCategory(restaurantName, category)
                .stream()
                .sorted(Comparator.comparing(DishDTO::getDishId))
                .peek(dish -> dish.setCategory(null))
                .peek(dish -> dish.setDishId(null))
                .toList();

        if (dishes.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(dishes);
    }
}
