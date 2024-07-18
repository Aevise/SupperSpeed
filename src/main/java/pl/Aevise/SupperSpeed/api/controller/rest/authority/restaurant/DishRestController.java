package pl.Aevise.SupperSpeed.api.controller.rest.authority.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import pl.Aevise.SupperSpeed.business.DishService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.domain.Dish;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(URLConstants.API_AUTH_RESTAURANT)
@AllArgsConstructor
@Tag(name = "Secured Dish Controller", description = "Endpoints for restaurants to manage their dishes")
public class DishRestController {

    public static final String ADD_DISH = "/dish/add";
    public static final String GET_DISHES = "/dish/all";
    public static final String UPDATE_DISH = "/dish/update";
    public static final String DELETE_DISH = "/dish/delete";

    private final DishService dishService;
    private final RestaurantService restaurantService;
    private final DishMapper dishMapper;

    @PostMapping(value = ADD_DISH, consumes = "application/json", produces = "application/json")
    @Operation(summary = "Add a new dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid dish category",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishDTO.class)))
    })
    public ResponseEntity<DishDTO> addNewDish(
            @Parameter(description = "Dish details", required = true)
            @RequestBody DishDTO dishDTO,
            @Parameter(hidden = true)
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
    @Operation(summary = "Get all dishes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of dishes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishDTO.class))),
            @ApiResponse(responseCode = "404", description = "No dishes found",
                    content = @Content)
    })
    public ResponseEntity<List<DishDTO>> getAllDishes(
            @Parameter(hidden = true)
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
    @Operation(summary = "Update a dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the dish",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden to modify this dish",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Incorrect parameters in request",
                    content = @Content)
    })
    public ResponseEntity<DishDTO> updateDish(
            @Parameter(description = "Details of the dish to be updated", required = true)
            @RequestBody DishDTO dishDTO,
            @Parameter(description = "ID of the dish to be updated", required = true)
            @RequestParam(value = "dishId") Integer dishId,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        checkDishData(dishDTO);
        if (dishId == null || dishId < 1) throw new IncorrectParamsInRESTRequest("No dish Id provided");

        Dish oldDish = dishService.findByIdPOJO(dishId);
        if (oldDish == null) throw new IncorrectParamsInRESTRequest("Dish not found");

        String username = userDetails.getUsername();
        RestaurantDTO restaurant = restaurantService.findRestaurantByEmail(username);

        if (!Objects.equals(oldDish.getRestaurant().getId(), restaurant.getRestaurantId())) {
            throw new ForbiddenRESTRequest("Can not modify this dish");
        }

        dishDTO.setDishId(oldDish.getDishId());
        DishDTO newDish = dishService.updateDish(dishDTO);

        return ResponseEntity.ok(newDish);
    }

    @DeleteMapping(DELETE_DISH)
    @Operation(summary = "Delete a dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the dish",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden to delete this dish",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<String> deleteDish(
            @Parameter(description = "ID of the dish to be deleted", required = true)
            @RequestParam(value = "dishId") Integer dishId,
            @Parameter(hidden = true)
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
