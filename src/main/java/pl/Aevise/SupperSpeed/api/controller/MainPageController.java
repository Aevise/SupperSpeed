package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
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

import java.util.List;

@Controller
@AllArgsConstructor
public class MainPageController {
    
    private static final String MAIN_PAGE = "/search";
    private final CuisineService cuisineService;
    private final CuisineMapper cuisineMapper;
    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;
    private final AddressService addressService;
    private final AddressMapper addressMapper;
    
    @GetMapping(MAIN_PAGE)
    public String getMainPage(){

        List<CuisineDTO> cuisines = getCuisineDTOList();
        List<RestaurantDTO> restaurants = getRestaurantsDTOList();
        List<AddressDTO> addresses = getAddressDTOList();


        return "main_page";
    }

    private List<AddressDTO> getAddressDTOList(){
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
}
