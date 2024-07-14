package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.SearchPageController.SEARCH_PAGE;
import static pl.Aevise.SupperSpeed.util.Constants.*;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.cuisineDTO2;

@WebMvcTest(controllers = SearchPageController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser
class SearchPageControllerMockMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private SecurityService securityService;
    @MockBean
    private CuisineService cuisineService;
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private AddressService addressService;
    @MockBean
    private SupperOrderService supperOrderService;

    @Test
    void getSearchPage() throws Exception {
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

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(SEARCH_PAGE)
                        .param("city", city)
                        .param("streetName", streetName)
                        .param("cuisine", cuisine)
                        .param("currDirection", currDirection)
                        .param("currPage", Integer.toString(currPage))
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("restaurantsByCuisine"))
                .andExpect(model().attributeExists("distinctCities"))
                .andExpect(model().attribute("distinctCities", distinctCities))
                .andExpect(model().attributeExists("role"))
                .andExpect(model().attributeExists("restaurantRatings"))
                .andExpect(model().attribute("restaurantRatings", restaurantsRating))
                .andExpect(model().attributeExists("cuisines"))
                .andExpect(model().attributeExists("cuisinesInCity"))
                .andExpect(model().attribute("cuisinesInCity", cuisinesInArea))
                .andExpect(model().attributeExists("currentCity"))
                .andExpect(model().attribute("currentCity", city))
                .andExpect(model().attributeExists("streetName"))
                .andExpect(model().attribute("streetName", streetName))
                .andExpect(model().attributeExists("currentCuisine"))
                .andExpect(model().attributeExists("totalNumberOfPages"))
                .andExpect(model().attributeExists("currentDirection"))
                .andExpect(model().attribute("currentDirection", currDirection))
                .andExpect(view().name("search_page"));
    }
}