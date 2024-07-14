package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ExtendedModelMap;
import pl.Aevise.SupperSpeed.api.controller.utils.PaginationAndSortingUtils;
import pl.Aevise.SupperSpeed.api.dto.CuisineDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.CuisineService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.business.SupperOrderService;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;

import java.util.List;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static pl.Aevise.SupperSpeed.util.Constants.*;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;

@ExtendWith(MockitoExtension.class)
class SearchPageControllerMockitoTest {

    @InjectMocks
    SearchPageController searchPageController;
    @Mock
    private SecurityService securityService;
    @Mock
    private CuisineService cuisineService;
    @Mock
    private RestaurantService restaurantService;
    @Mock
    private AddressService addressService;
    @Mock
    private SupperOrderService supperOrderService;

    @Test
    void checkThatYouCanGetSpecificRestaurantsByCuisine() {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        CuisineDTO cuisineDTO1 = cuisineDTO1();
        restaurantDTO.setCuisine(cuisineDTO1);
        String city = WARSZAWA;
        String streetName = "Jaskrawa";
        String cuisine = cuisineDTO1.getCuisine();
        String currDirection = "asc";
        int currPage = 0;
        String userRole = "CLIENT";

        String sortingDirection = PaginationAndSortingUtils.ASC.getSortingDirection();
        List<CuisineDTO> cuisineDTOS = List.of(cuisineDTO1, cuisineDTO2());
        List<String> distinctCities = List.of(WARSZAWA, LUBLIN, CHELM);
        PageImpl<RestaurantDTO> availableRestaurants = new PageImpl<>(List.of(restaurantDTO));
        TreeMap<Integer, List<Double>> restaurantsRating = new TreeMap<>();
        restaurantsRating.put(restaurantDTO.getRestaurantId(), List.of(1.0, 1.0, 1.0));
        List<String> cuisinesInArea = List.of(cuisineDTO1.getCuisine(), cuisineDTO2().getCuisine());

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(cuisineService.findAllSorted(sortingDirection)).thenReturn(cuisineDTOS);
        when(addressService.findDistinctCities()).thenReturn(distinctCities);
        when(securityService.getUserAuthority()).thenReturn(userRole);
        when(restaurantService.findAllByCityAndStreetNameAndCuisineOnDelivery(city, streetName, cuisine,
                PageRequest.of(currPage, 10,
                        Sort.by("restaurantEntity.cuisine.cuisine")
                                .and(Sort.by("restaurantEntity.restaurantName"))
                                .ascending()))).thenReturn(availableRestaurants);
        when(supperOrderService.getRestaurantsRatingBasedOnOrders(availableRestaurants.toList())).thenReturn(restaurantsRating);
        when(restaurantService.findCuisinesByDeliveryAddress_CityAndStreetName(city, streetName)).thenReturn(cuisinesInArea);

        String result = searchPageController.getSearchPage(model, city, streetName, cuisine, currDirection, currPage);

        //then
        assertEquals(result, "search_page");
        assertThat(model.get("restaurantsByCuisine")).isNotNull();
        assertThat(model.get("distinctCities")).isNotNull().isEqualTo(distinctCities);
        assertThat(model.get("role")).isNotNull().isEqualTo(userRole);
        assertThat(model.get("restaurantRatings")).isNotNull().isEqualTo(restaurantsRating);
        assertThat(model.get("cuisines")).isNotNull().isEqualTo(cuisineDTOS);
        assertThat(model.get("cuisinesInCity")).isNotNull().isEqualTo(cuisinesInArea);

        assertThat(model.get("currentCity")).isNotNull().isEqualTo(city);
        assertThat(model.get("streetName")).isNotNull().isEqualTo(streetName);
        assertThat(model.get("currentCuisine")).isNotNull().isEqualTo(cuisine);
        assertThat(model.get("totalNumberOfPages")).isNotNull().isEqualTo(availableRestaurants.getTotalPages());
        assertThat(model.get("currentDirection")).isNotNull().isEqualTo(currDirection);
        assertThat(model.get("currentPage")).isNotNull().isEqualTo(currPage);
    }

    @Test
    void checkThatYouCanGetAllRestaurantByNonExistentCuisine() {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        CuisineDTO cuisineDTO1 = cuisineDTO1();
        restaurantDTO.setCuisine(cuisineDTO1);
        String city = WARSZAWA;
        String streetName = "Jaskrawa";
        String cuisine = "asere he a hehe";
        String currDirection = "asc";
        int currPage = 0;
        String userRole = "CLIENT";

        String sortingDirection = PaginationAndSortingUtils.ASC.getSortingDirection();
        List<CuisineDTO> cuisineDTOS = List.of(cuisineDTO1, cuisineDTO2());
        List<String> distinctCities = List.of(WARSZAWA, LUBLIN, CHELM);
        PageImpl<RestaurantDTO> availableRestaurants = new PageImpl<>(List.of(restaurantDTO));
        TreeMap<Integer, List<Double>> restaurantsRating = new TreeMap<>();
        restaurantsRating.put(restaurantDTO.getRestaurantId(), List.of(1.0, 1.0, 1.0));
        List<String> cuisinesInArea = List.of(cuisineDTO1.getCuisine(), cuisineDTO2().getCuisine());

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(cuisineService.findAllSorted(sortingDirection)).thenReturn(cuisineDTOS);
        when(addressService.findDistinctCities()).thenReturn(distinctCities);
        when(securityService.getUserAuthority()).thenReturn(userRole);
        when(restaurantService.findAllByCityAndStreetNameOnDelivery(city, streetName,
                PageRequest.of(currPage, 10,
                        Sort.by("restaurantEntity.cuisine.cuisine")
                                .and(Sort.by("restaurantEntity.restaurantName"))
                                .ascending()))).thenReturn(availableRestaurants);
        when(supperOrderService.getRestaurantsRatingBasedOnOrders(availableRestaurants.toList())).thenReturn(restaurantsRating);
        when(restaurantService.findCuisinesByDeliveryAddress_CityAndStreetName(city, streetName)).thenReturn(cuisinesInArea);

        String result = searchPageController.getSearchPage(model, city, streetName, cuisine, currDirection, currPage);

        //then
        assertEquals(result, "search_page");
        assertThat(model.get("restaurantsByCuisine")).isNotNull();
        assertThat(model.get("distinctCities")).isNotNull().isEqualTo(distinctCities);
        assertThat(model.get("role")).isNotNull().isEqualTo(userRole);
        assertThat(model.get("restaurantRatings")).isNotNull().isEqualTo(restaurantsRating);
        assertThat(model.get("cuisines")).isNotNull().isEqualTo(cuisineDTOS);
        assertThat(model.get("cuisinesInCity")).isNotNull().isEqualTo(cuisinesInArea);

        assertThat(model.get("currentCity")).isNotNull().isEqualTo(city);
        assertThat(model.get("streetName")).isNotNull().isEqualTo(streetName);
        assertThat(model.get("currentCuisine")).isNotNull().isEqualTo("All");
        assertThat(model.get("totalNumberOfPages")).isNotNull().isEqualTo(availableRestaurants.getTotalPages());
        assertThat(model.get("currentDirection")).isNotNull().isEqualTo(currDirection);
        assertThat(model.get("currentPage")).isNotNull().isEqualTo(currPage);
    }
}