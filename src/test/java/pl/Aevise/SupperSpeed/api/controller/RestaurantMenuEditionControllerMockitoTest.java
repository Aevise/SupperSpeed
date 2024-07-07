package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ExtendedModelMap;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.*;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.MAX_IMAGE_HEIGHT;
import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.MAX_IMAGE_WIDTH;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.testPassword;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;

@ExtendWith(MockitoExtension.class)
class RestaurantMenuEditionControllerMockitoTest {
    @Mock
    private DishListService dishListService;
    @Mock
    private DishCategoryService dishCategoryService;
    @Mock
    private DishService dishService;
    @Mock
    private RestaurantService restaurantService;
    @Mock
    private ImageHandlingService imageHandlingService;

    @InjectMocks
    RestaurantMenuEditionController restaurantMenuEditionController;

    @Test
    void checkThatYouCanGetRestaurantMenuInEditionMode() {
        //given
        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_1).password(testPassword).authorities("RESTAURANT").build();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Integer restaurantId = restaurantDTO.getRestaurantId();
        String restaurantName = restaurantDTO.getRestaurantName();
        List<DishCategoryDTO> dishCategories = List.of(dishCategoryDTO1(), dishCategoryDTO2());

        HashMap<List<DishCategoryDTO>, List<DishDTO>> dishMap = new HashMap<>();
        dishMap.putIfAbsent(List.of(dishCategoryDTO1()), List.of(dishDTO1()));
        String restaurantDirectory = "testName";

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurantDTO);
        when(dishListService.getDishCategoriesByRestaurantId(restaurantId)).thenReturn(dishCategories);
        when(dishService.extractDishesByCategory(dishCategories, false)).thenReturn(dishMap);
        when(imageHandlingService.getRestaurantName(restaurantId, restaurantName)).thenReturn(restaurantDirectory);

        String result = restaurantMenuEditionController.getRestaurantMenuEdit(model, userDetails);

        //then
        assertThat(result).isNotNull().isEqualTo("restaurant_menu_edit");
        assertThat(model.get("dishesByCategory")).isNotNull().isEqualTo(dishMap);
        assertThat(model.get("restaurantId")).isNotNull().isEqualTo(restaurantId);
        assertThat(model.get("categories")).isNotNull().isEqualTo(dishCategories);
        assertThat(model.get("restaurantDirectory")).isNotNull().isEqualTo(restaurantDirectory);
        assertThat(model.get("imageWidth")).isNotNull().isEqualTo(MAX_IMAGE_WIDTH);
        assertThat(model.get("imageHeight")).isNotNull().isEqualTo(MAX_IMAGE_HEIGHT);
        assertThat(model.get("restaurantName")).isNotNull().isEqualTo(restaurantName);
    }
}