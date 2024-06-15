package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.controller.utils.PaginationAndSortingUtils;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.CuisineDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.CuisineMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.RestaurantMapper;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.CuisineService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.business.SupperOrderService;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.domain.Cuisine;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
public class SearchPageController {

    private static final String SEARCH_PAGE = "/search";

    private final SecurityService securityService;

    private final CuisineService cuisineService;
    private final CuisineMapper cuisineMapper;

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    private final SupperOrderService supperOrderService;

    @GetMapping(SEARCH_PAGE)
    public String getSearchPage
            (
                    Model model,
                    @RequestParam(value = "city") String city,
                    @RequestParam(value = "streetName") String streetName,
                    @RequestParam(value = "cuisine", required = false) String cuisine
            ) {

        List<CuisineDTO> cuisines = cuisineService.findAllSorted(PaginationAndSortingUtils.ASC.getSortingDirection());



        //to chyba mozna usunac bez zadnych obaw
        List<AddressDTO> addresses = getAddressDTOList();





        List<RestaurantDTO> restaurants = getRestaurantsByCityDTOList(city);

        List<String> cities = addressService.findDistinctCities();

        String userRole = securityService.getUserAuthority();

        var restaurantsByCuisine = mapRestaurantsByCuisine(restaurants);
        var restaurantsRating = supperOrderService.getRestaurantsRatingBasedOnOrders(restaurants);


        model.addAttribute("restaurantsByCuisine", restaurantsByCuisine);
        model.addAttribute("currentCity", city);
        model.addAttribute("distinctCities", cities);
        model.addAttribute("role", userRole);
        model.addAttribute("restaurantRatings", restaurantsRating);
        model.addAttribute("cuisines", cuisines);
        model.addAttribute("streetName", streetName);
        return "search_page";
    }

    private List<AddressDTO> getAddressDTOList() {
        List<Address> address = addressService.findAll();

        return address.stream()
                .map(addressMapper::mapToDTO)
                .toList();
    }

    private List<RestaurantDTO> getRestaurantsByCityDTOList(String city) {
        List<Restaurant> restaurants = restaurantService.findAllByCity(city);

        return restaurants.stream()
                .map(restaurantMapper::mapToDTO)
                .toList();
    }

    private HashMap<String, List<RestaurantDTO>> mapRestaurantsByCuisine(List<RestaurantDTO> restaurants) {
        HashMap<String, List<RestaurantDTO>> restaurantsByCuisine = new HashMap<>();

        for (RestaurantDTO restaurant : restaurants) {
            if (restaurant.getIsShown()) {
                restaurantsByCuisine.putIfAbsent(restaurant.getCuisine().getCuisine(), new ArrayList<>());
                restaurantsByCuisine.get(restaurant.getCuisine().getCuisine()).add(restaurant);
            }
        }
        return restaurantsByCuisine;
    }
}
