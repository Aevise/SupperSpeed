package pl.Aevise.SupperSpeed.api.controller.rest;

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
public class SearchRestController {

    public static final String SEARCH_ENDPOINT = "/search";
    private final RestaurantService restaurantService;


    @GetMapping(SEARCH_ENDPOINT)
    public ResponseEntity<List<RestRestaurantDTO>> searchForRestaurantByAddress(
            @RequestParam(value = "city") String city,
            @RequestParam(value = "cuisine", required = false) String cuisine
    ) {
        List<RestaurantDTO> restaurantsDTO;
        if(cuisine == null){
            restaurantsDTO = restaurantService.findAllByCity(city);
        }
        else {
            restaurantsDTO = restaurantService.findAllByCityAndCuisine(city, cuisine);
        }

        List<RestRestaurantDTO> restaurants = restaurantService.prepareDataToRest(restaurantsDTO);

        if (restaurants.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(restaurants);
    }
}
