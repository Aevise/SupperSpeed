package pl.Aevise.SupperSpeed.api.controller.rest.authority.restaurant;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.Aevise.SupperSpeed.api.controller.exception.ForbiddenRESTRequest;
import pl.Aevise.SupperSpeed.api.controller.exception.IncorrectParamsInRESTRequest;
import pl.Aevise.SupperSpeed.api.controller.utils.URLConstants;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.DishMapper;
import pl.Aevise.SupperSpeed.business.DishCategoryService;
import pl.Aevise.SupperSpeed.business.DishService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.domain.Dish;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(URLConstants.API_AUTH_RESTAURANT)
@AllArgsConstructor
public class DishRestController {

    public static final String ADD_DISH = "/dish/add";
    public static final String GET_DISHES = "/dish/all";
    public static final String UPDATE_DISH = "/dish/update";
    public static final String DELETE_DISH = "/dish/delete";

    private final DishCategoryService dishCategoryService;
    private final DishService dishService;
    private final RestaurantService restaurantService;
    private final DishMapper dishMapper;

    @PostMapping(value = ADD_DISH, consumes = "application/json", produces = "application/json")
    public ResponseEntity<DishDTO> addNewDish(
            @RequestBody DishDTO dishDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        RestaurantDTO restaurantByEmail = restaurantService.findRestaurantByEmail(username);

        Dish dish = dishService.buildDish(dishDTO, restaurantByEmail.getRestaurantId(), dishDTO.getCategory());
        if (dish.getDishCategory() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dishDTO);
        }
        Dish newDish = dishService.addDish(dish);
        DishDTO newDishDTO = dishMapper.mapToDTO(newDish);

        return ResponseEntity.status(HttpStatus.CREATED).body(newDishDTO);
    }

    @GetMapping(value = GET_DISHES)
    public ResponseEntity<List<DishDTO>> getAllDishes(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        RestaurantDTO restaurant = restaurantService.findRestaurantByEmail(username);

        List<DishDTO> dishes = dishService.findAllNotHiddenDishes(restaurant.getRestaurantName())
                .stream()
                .sorted(Comparator.comparing(DishDTO::getDishId))
                .toList();
        if (dishes.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(dishes);
    }

    @PutMapping(value = UPDATE_DISH)
    public ResponseEntity<DishDTO> updateDish(
            @RequestBody DishDTO dishDTO,
            @RequestParam(value = "dishId") Integer dishId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        checkDishData(dishDTO);

        String username = userDetails.getUsername();
        RestaurantDTO restaurant = restaurantService.findRestaurantByEmail(username);

        Dish oldDish = dishService.findByIdPOJO(dishId);

        if (oldDish == null ||
                !Objects.equals(oldDish.getRestaurant().getId(), restaurant.getRestaurantId())) {
            throw new ForbiddenRESTRequest("Can not modify this dish");
        }

        dishDTO.setDishId(oldDish.getDishId());
        DishDTO newDish = dishService.updateDish(dishDTO);

        return ResponseEntity.ok(newDish);
    }

    @DeleteMapping(DELETE_DISH)
    public ResponseEntity<String> deleteDish(
            @RequestParam(value = "dishId") Integer dishId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        RestaurantDTO restaurant = restaurantService.findRestaurantByEmail(username);

        Dish dish = dishService.findByIdPOJO(dishId);
        if (dish == null || !Objects.equals(dish.getRestaurant().getId(), restaurant.getRestaurantId()) || dish.getIsHidden()) {
            throw new ForbiddenRESTRequest("You can not delete this dish");
        }
        dishService.deleteOrHideDishByDishId(dishId);

        Dish newDishData = dishService.findByIdPOJO(dishId);
        if (newDishData == null || newDishData.getIsHidden()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Dish deleted.");
        }
        return ResponseEntity.internalServerError().build();
    }

    private void checkDishData(DishDTO dishDTO) {
        if (dishDTO.getName() == null) {
            throw new IncorrectParamsInRESTRequest("Body must contain name value");
        }
        if (dishDTO.getDescription() == null) {
            throw new IncorrectParamsInRESTRequest("Body must contain description value");
        }
        if (dishDTO.getPrice() == null) {
            throw new IncorrectParamsInRESTRequest("Body must contain price value");
        }
    }
}
