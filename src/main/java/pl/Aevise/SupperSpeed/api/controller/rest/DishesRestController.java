package pl.Aevise.SupperSpeed.api.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.business.DishService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(DishesRestController.DISH)
@AllArgsConstructor
public class DishesRestController {

    public static final String DISH = "/api/unauth/dishes";
    public static final String ALL_DISHES_FROM_RESTAURANT = "/{restaurantName}/category/all";
    public static final String ALL_DISHES_FROM_RESTAURANT_BY_CATEGORY = "/{restaurantName}/category/{category}";

    private final DishService dishService;

    @GetMapping(value = ALL_DISHES_FROM_RESTAURANT)
    public ResponseEntity<List<DishDTO>> allDishesFromRestaurant(
            @PathVariable String restaurantName
    ){
        if (Objects.isNull(restaurantName)) {
            return ResponseEntity.notFound().build();
        }

        List<DishDTO> dishes = dishService.findAllNotHiddenDishes(restaurantName)
                .stream().sorted(Comparator.comparing(DishDTO::getDishId)).toList();
        if(dishes.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(dishes);
    }

    @GetMapping(value = ALL_DISHES_FROM_RESTAURANT_BY_CATEGORY)
    public ResponseEntity<List<DishDTO>> dishesFromRestaurantByCategory(
            @PathVariable String restaurantName,
            @PathVariable String category
    ){
        if (Objects.isNull(restaurantName)) {
            return ResponseEntity.notFound().build();
        }

        List<DishDTO> dishes = dishService.findNotHiddenDishesByCategory(restaurantName, category)
                .stream()
                .peek(dish -> dish.setCategory(null))
                .sorted(Comparator.comparing(DishDTO::getDishId)).toList();
        if(dishes.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(dishes);
    }
}
