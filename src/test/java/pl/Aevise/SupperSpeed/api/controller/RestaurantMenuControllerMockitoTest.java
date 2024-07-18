package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import pl.Aevise.SupperSpeed.api.controller.exception.UserNotFoundException;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.RestaurantMapper;
import pl.Aevise.SupperSpeed.business.DishListService;
import pl.Aevise.SupperSpeed.business.DishService;
import pl.Aevise.SupperSpeed.business.ImageHandlingService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;
import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.MAX_LOGO_HEIGHT;
import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.MAX_LOGO_WIDTH;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.restaurant1;

@ExtendWith(MockitoExtension.class)
class RestaurantMenuControllerMockitoTest {

    @InjectMocks
    RestaurantMenuController restaurantMenuController;
    @Mock
    private DishService dishService;
    @Mock
    private DishListService dishListService;
    @Mock
    private ImageHandlingService imageHandlingService;
    @Mock
    private RestaurantService restaurantService;
    @Mock
    private RestaurantMapper restaurantMapper;
    @Mock
    private SecurityService securityService;

    @Test
    void checkThatShouldReturnErrorPageWhenUserWithNegativeIdIsSelected() {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        int restaurantId = -2;
        String restaurantName = restaurantDTO.getRestaurantName();
        String expectedErrorMessage = "Wrong identification provided. User Not Found.";

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        UserNotFoundException expectedException = assertThrows(UserNotFoundException.class,
                () -> restaurantMenuController.getRestaurantMenu(restaurantId, restaurantName, model));

        //then
        assertThat(expectedErrorMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    void checkThatCorrectRestaurantMenuPageIsDisplayed() {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        int restaurantId = restaurantDTO.getRestaurantId();
        Restaurant restaurant = restaurant1();
        String restaurantName = restaurantDTO.getRestaurantName();
        List<DishCategoryDTO> dishCategories = List.of(dishCategoryDTO1(), dishCategoryDTO2());

        HashMap<List<DishCategoryDTO>, List<DishDTO>> dishMap = new HashMap<>();
        dishMap.putIfAbsent(List.of(dishCategoryDTO1()), List.of(dishDTO1()));
        String restaurantDirectory = "testName";
        String role = AvailableRoles.CLIENT.toString();


        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(dishListService.getDishCategoriesByRestaurantId(restaurantId)).thenReturn(dishCategories);
        when(dishService.extractDishesByCategory(dishCategories, true)).thenReturn(dishMap);
        when(restaurantService.findRestaurantById(restaurantId)).thenReturn(restaurant);
        when(restaurantMapper.mapToDTO(restaurant)).thenReturn(restaurantDTO);
        when(imageHandlingService.getRestaurantName(restaurantId, restaurantName)).thenReturn(restaurantDirectory);
        when(securityService.getUserAuthority()).thenReturn(role);

        String result = restaurantMenuController.getRestaurantMenu(restaurantId, restaurantName, model);

        //then
        assertThat(result).isNotNull().isEqualTo("restaurant_menu");
        assertThat(model.get("dishesByCategory")).isNotNull().isEqualTo(dishMap);
        assertThat(model.get("restaurantId")).isNotNull().isEqualTo(restaurantId);
        assertThat(model.get("imageWidth")).isNotNull().isEqualTo(MAX_LOGO_WIDTH);
        assertThat(model.get("imageHeight")).isNotNull().isEqualTo(MAX_LOGO_HEIGHT);
        assertThat(model.get("restaurantDirectory")).isNotNull().isEqualTo(restaurantDirectory);
        assertThat(model.get("restaurantName")).isNotNull().isEqualTo(restaurantName);
        assertThat(model.get("addressDTO")).isNotNull().isEqualTo(restaurantDTO.getAddress());
        assertThat(model.get("userRole")).isNotNull().isEqualTo(role);
    }
}