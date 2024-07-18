package pl.Aevise.SupperSpeed.api.controller.rest.noAuthority;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.Aevise.SupperSpeed.api.controller.utils.URLConstants;
import pl.Aevise.SupperSpeed.api.dto.RestRestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.RestaurantService;

import java.util.List;

@RestController
@RequestMapping(URLConstants.API_UNAUTH)
@AllArgsConstructor
@Tag(name = "Restaurant Search Controller", description = "Endpoints for searching restaurants")
public class SearchRestController {

    public static final String SEARCH_ENDPOINT = "/search";
    private final RestaurantService restaurantService;


    @GetMapping(SEARCH_ENDPOINT)
    @Operation(summary = "Get all restaurant by given params")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of restaurants"),
            @ApiResponse(responseCode = "404", description = "No restaurants found with the given parameters")
    })
    public ResponseEntity<List<RestRestaurantDTO>> searchForRestaurantByAddress(
            @Parameter(description = "Name of the city", required = true)
            @RequestParam(value = "city") String city,
            @Parameter(description = "Searched cuisine", required = true)
            @RequestParam(value = "cuisine", required = false) String cuisine
    ) {
        List<RestaurantDTO> restaurantsDTO;
        if (cuisine == null) {
            restaurantsDTO = restaurantService.findAllByCity(city);
        } else {
            restaurantsDTO = restaurantService.findAllByCityAndCuisine(city, cuisine);
        }

        List<RestRestaurantDTO> restaurants = restaurantService.prepareDataToRest(restaurantsDTO);

        if (restaurants.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(restaurants);
    }
}
