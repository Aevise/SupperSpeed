package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.controller.utils.PaginationAndSortingUtils;
import pl.Aevise.SupperSpeed.api.dto.CuisineDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.CuisineMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.RestaurantMapper;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.CuisineService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.business.SupperOrderService;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
                    @RequestParam(value = "cuisine", required = false, defaultValue = "All") String cuisine,
                    @RequestParam(value = "currDirection", required = false, defaultValue = "asc") String currDirection,
                    @RequestParam(value = "currPage", required = false, defaultValue = "0") Integer currPage
            ) {
        List<CuisineDTO> cuisines = cuisineService.findAllSorted(PaginationAndSortingUtils.ASC.getSortingDirection());
        String finalCuisine = cuisine;
        if (cuisines.stream().noneMatch(cuisineDTO -> cuisineDTO.getCuisine().equalsIgnoreCase(finalCuisine))) {
            cuisine = "All";
        }
        List<String> cities = addressService.findDistinctCities();
        String userRole = securityService.getUserAuthority();

//        Page<RestaurantDTO> allByCityAndStreetNameOnDelivery = restaurantService.findAllByCityAndStreetNameOnDelivery(city, streetName, buildPageRequestForRestaurant("asc", 0));
//        List<String> cuisinesInArea = mapRestaurantsByCuisine(allByCityAndStreetNameOnDelivery).keySet()
//                .stream()
//                .sorted(String::compareTo)
//                .toList();
//
//        TreeMap<String, List<RestaurantDTO>> restaurantsByCuisine;
//        TreeMap<Integer, List<Double>> restaurantsRating;
//        if(!cuisine.equalsIgnoreCase("all")){
//            List<RestaurantDTO> filteredRestaurants = restaurantService.filterRestaurantDTOsByCuisine(cuisine, allByCityAndStreetNameOnDelivery);
//            restaurantsByCuisine = mapRestaurantsByCuisine(filteredRestaurants);
//            restaurantsRating = supperOrderService.getRestaurantsRatingBasedOnOrders(filteredRestaurants);
//        }else {
//            restaurantsByCuisine = mapRestaurantsByCuisine(allByCityAndStreetNameOnDelivery);
//            restaurantsRating = supperOrderService.getRestaurantsRatingBasedOnOrders(allByCityAndStreetNameOnDelivery);
//        }


        //-----------------------
        Page<RestaurantDTO> availableRestaurants;
        if (cuisine.equalsIgnoreCase("all")) {
            availableRestaurants = restaurantService.findAllByCityAndStreetNameOnDelivery(city,
                    streetName,
                    buildPageRequestForRestaurant(currDirection, currPage));

        } else {
            availableRestaurants = restaurantService.findAllByCityAndStreetNameAndCuisineOnDelivery(city,
                    streetName,
                    cuisine,
                    buildPageRequestForRestaurant(currDirection, currPage));
        }

        TreeMap<String, List<RestaurantDTO>> restaurantsByCuisine = mapRestaurantsByCuisine(availableRestaurants.toList());
        TreeMap<Integer, List<Double>> restaurantsRating = supperOrderService.getRestaurantsRatingBasedOnOrders(availableRestaurants.toList());
        List<String> cuisinesInArea = restaurantService.findCuisinesByDeliveryAddress_CityAndStreetName(city, streetName);
        int numberOfPages = availableRestaurants.getTotalPages();


        //-----------------------

        model.addAttribute("restaurantsByCuisine", restaurantsByCuisine);
        model.addAttribute("distinctCities", cities);
        model.addAttribute("role", userRole);
        model.addAttribute("restaurantRatings", restaurantsRating);
        model.addAttribute("cuisines", cuisines);
        model.addAttribute("cuisinesInCity", cuisinesInArea);

        model.addAttribute("currentCity", city);
        model.addAttribute("streetName", streetName);
        model.addAttribute("currentCuisine", cuisine);
        model.addAttribute("totalNumberOfPages", numberOfPages);
        model.addAttribute("currentDirection", currDirection);
        model.addAttribute("currentPage", currPage);


        return "search_page";
    }


    private TreeMap<String, List<RestaurantDTO>> mapRestaurantsByCuisine(List<RestaurantDTO> restaurants) {
        TreeMap<String, List<RestaurantDTO>> restaurantsByCuisine = new TreeMap<>();

        for (RestaurantDTO restaurant : restaurants) {
            if (restaurant.getIsShown()) {
                restaurantsByCuisine.putIfAbsent(restaurant.getCuisine().getCuisine(), new ArrayList<>());
                restaurantsByCuisine.get(restaurant.getCuisine().getCuisine()).add(restaurant);
            }
        }
        return restaurantsByCuisine;
    }

    private PageRequest buildPageRequestForRestaurant(String direction, Integer page) {
        if (direction.equalsIgnoreCase(PaginationAndSortingUtils.ASC.getSortingDirection())) {
            return PageRequest.of(page,
                    10,
                    Sort.by("restaurantEntity.cuisine.cuisine")
                            .and(Sort.by("restaurantEntity.restaurantName"))
                            .ascending());
        }
        return PageRequest.of(page,
                10,
                Sort.by("restaurantEntity.cuisine.cuisine")
                        .and(Sort.by("restaurantEntity.restaurantName"))
                        .descending());
    }
}
