package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.CuisineDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.CuisineMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.RestaurantMapper;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.CuisineService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.domain.Cuisine;
import pl.Aevise.SupperSpeed.domain.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
public class MainPageController {

    private static final String MAIN_PAGE = "/";

    private final CuisineService cuisineService;
    private final CuisineMapper cuisineMapper;
    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;
    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @GetMapping(MAIN_PAGE)
    public String getMainPage(Model model) {

        List<CuisineDTO> cuisines = getCuisineDTOList();
        List<AddressDTO> addresses = getAddressDTOList();
        List<RestaurantDTO> restaurants = getRestaurantsDTOList();

        HashMap<String, List<RestaurantDTO>> restaurantsByCuisine = mapRestaurantsByCuisine(restaurants);

        model.addAttribute("restaurantsByCuisine", restaurantsByCuisine);

        return "main_page";
    }

    private List<AddressDTO> getAddressDTOList() {
        List<Address> address = addressService.findAll();

        return address.stream()
                .map(addressMapper::mapToDTO)
                .toList();
    }

    private List<RestaurantDTO> getRestaurantsDTOList() {
        List<Restaurant> restaurants = restaurantService.findAll();

        return restaurants.stream()
                .map(restaurantMapper::mapToDTO)
                .toList();
    }

    private List<CuisineDTO> getCuisineDTOList() {
        List<Cuisine> cuisines = cuisineService.findAll();

        return cuisines.stream()
                .map(cuisineMapper::mapToDTO)
                .toList();
    }

    private HashMap<String, List<RestaurantDTO>> mapRestaurantsByCuisine(List<RestaurantDTO> restaurants) {
        HashMap<String, List<RestaurantDTO>> restaurantsByCuisine = new HashMap<>();

        for (RestaurantDTO restaurant : restaurants) {
            restaurantsByCuisine.putIfAbsent(restaurant.getCuisine().getCuisine(), new ArrayList<>());
            restaurantsByCuisine.get(restaurant.getCuisine().getCuisine()).add(restaurant);
        }
        return restaurantsByCuisine;
    }
}
