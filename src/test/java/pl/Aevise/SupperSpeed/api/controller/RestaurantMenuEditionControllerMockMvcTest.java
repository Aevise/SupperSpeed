package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.*;
import pl.Aevise.SupperSpeed.domain.Dish;
import pl.Aevise.SupperSpeed.domain.DishCategory;
import pl.Aevise.SupperSpeed.util.DTOFixtures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.RestaurantMenuEditionController.*;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.dish1;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.dishCategory1;

@WebMvcTest(controllers = RestaurantMenuEditionController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
class RestaurantMenuEditionControllerMockMvcTest {

    private final MockMvc mockMvc;

    @MockBean
    private DishListService dishListService;
    @MockBean
    private DishCategoryService dishCategoryService;
    @MockBean
    private DishService dishService;
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private ImageHandlingService imageHandlingService;


    @Test
    void checkThatYouCanGetRestaurantMenuEdition() throws Exception {
        //given
        RestaurantDTO restaurantDTO = DTOFixtures.restaurantDTO1();
        String restaurantDirectory = String.format(restaurantDTO.getRestaurantId() + "_" + restaurantDTO.getRestaurantName());

        List<DishCategoryDTO> dishCategoryDTOS = List.of(DTOFixtures.dishCategoryDTO1(), DTOFixtures.dishCategoryDTO2());
        HashMap<List<DishCategoryDTO>, List<DishDTO>> dishMap = new HashMap<>();
        dishMap.putIfAbsent(List.of(dishCategoryDTO1()), List.of(DTOFixtures.dishDTO1()));

        //when
        Mockito.when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurantDTO);
        Mockito.when(dishListService.getDishCategoriesByRestaurantId(restaurantDTO.getRestaurantId())).thenReturn(dishCategoryDTOS);
        Mockito.when(dishService.extractDishesByCategory(dishCategoryDTOS, false)).thenReturn(dishMap);
        Mockito.when(imageHandlingService.getRestaurantName(restaurantDTO.getRestaurantId(), restaurantDTO.getRestaurantName())).thenReturn(restaurantDirectory);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_MENU_EDIT)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("dishesByCategory"))
                .andExpect(model().attribute("dishesByCategory", dishMap))
                .andExpect(model().attributeExists("restaurantId"))
                .andExpect(model().attribute("restaurantId", restaurantDTO.getRestaurantId()))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("categories", dishCategoryDTOS))
                .andExpect(model().attributeExists("restaurantDirectory"))
                .andExpect(model().attribute("restaurantDirectory", restaurantDirectory))
                .andExpect(model().attributeExists("restaurantName"))
                .andExpect(model().attribute("restaurantName", restaurantDTO.getRestaurantName()))
                .andExpect(view().name("restaurant_menu_edit"));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatErrorIsThrownWhenUserWithoutPermissionTriesToUpdateDishInformation() throws Exception {
        //given
        DishDTO dishDTO = dishDTO1();
        String expectedErrorMessage = "Error:\n[You do not have the required authority to update this dish.]";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = dishDTO.asMap();
        parametersMap.forEach(params::add);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_UPDATE_DISH)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", expectedErrorMessage))
                .andExpect(view().name("error"));
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanCorrectlyUpdateDish(
            Boolean available
    ) throws Exception {
        //given
        DishDTO dishDTO = dishDTO1();
        dishDTO.setAvailability(available);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = dishDTO.asMap();
        parametersMap.forEach(params::add);

        //when
        doNothing().when(dishService).updateDish(dishDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_UPDATE_DISH)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));
    }

    public static Stream<Boolean> checkThatYouCanCorrectlyUpdateDish() {
        return Stream.of(false, true);
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatErrorIsThrownWhenYouTryToDeleteDishWithoutAuthority() throws Exception {
        //given
        DishDTO dishDTO = dishDTO1();
        String expectedErrorMessage = "Error:\n[You do not have the required authority to delete this dish.]";

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_DELETE_DISH)
                .param("dishId", dishDTO.getDishId().toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", expectedErrorMessage))
                .andExpect(view().name("error"));
    }

    @Test
    void checkThatYouCanCorrectlyDeleteDish() throws Exception {
        //given
        DishDTO dishDTO = dishDTO1();

        //when
        doNothing().when(dishService).deleteOrHideDishByDishId(dishDTO.getDishId());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_DELETE_DISH)
                .param("dishId", dishDTO.getDishId().toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatExceptionIsThrownWhenYouTryToDeleteDishCategoryWithoutAuthority() throws Exception {
        //given
        DishCategoryDTO dishCategoryDTO = dishCategoryDTO1();
        String expectedErrorMessage = "Error:\n[You do not have the required authority to delete this dish category.]";

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_DELETE_CATEGORY)
                .param("dishCategoryId", dishCategoryDTO.getDishCategoryId().toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", expectedErrorMessage))
                .andExpect(view().name("error"));
    }

    @Test
    void checkThatYouCanDeleteCategoryWithoutDishes() throws Exception {
        //given
        DishCategoryDTO dishCategoryDTO = dishCategoryDTO1();
        Integer dishCategoryId = dishCategoryDTO.getDishCategoryId();

        //when
        when(dishService.findAllByCategory(dishCategoryId)).thenReturn(List.of());
        doNothing().when(dishCategoryService).deleteCategory(dishCategoryId);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_DELETE_CATEGORY)
                .param("dishCategoryId", dishCategoryId.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));
    }

    @Test
    void checkThatYouCanDeleteCategoryWithDishes() throws Exception {
        //given
        DishCategoryDTO dishCategoryDTO = dishCategoryDTO1();
        Integer dishCategoryId = dishCategoryDTO.getDishCategoryId();
        List<Dish> dishes = List.of(dish1());

        //when
        when(dishService.findAllByCategory(dishCategoryId)).thenReturn(dishes);
        doNothing().when(dishService).deleteOrHideDishesMap(any());
        doNothing().when(dishCategoryService).deleteCategory(dishCategoryId);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_DELETE_CATEGORY)
                .param("dishCategoryId", dishCategoryId.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatExceptionIsThrownWhenYouTryToUpdateCategoryWithoutAuthority() throws Exception {
        //given
        DishCategoryDTO dishCategoryDTO = dishCategoryDTO1();
        String expectedErrorMessage = "Error:\n[You do not have the required authority to update this category.]";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = dishCategoryDTO.asMap();
        parametersMap.forEach(params::add);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_UPDATE_CATEGORY)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", expectedErrorMessage))
                .andExpect(view().name("error"));
    }

    @Test
    void checkThatYouCanUpdateCategory() throws Exception {
        //given
        DishCategoryDTO dishCategoryDTO = dishCategoryDTO1();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = dishCategoryDTO.asMap();
        parametersMap.forEach(params::add);

        //when
        doNothing().when(dishCategoryService).updateCategory(dishCategoryDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_UPDATE_CATEGORY)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatExceptionIsThrownWhenYouTryToAddCategoryWithoutAuthorization() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        DishCategoryDTO dishCategoryDTO = dishCategoryDTO1();

        Integer restaurantId = restaurantDTO.getRestaurantId();
        String categoryName = dishCategoryDTO.getCategoryName();
        String expectedErrorMessage = "Error:\n[You do not have the required authority to add new category.]";

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_ADD_CATEGORY)
                .param("categoryName", categoryName)
                .param("restaurantId", restaurantId.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", expectedErrorMessage))
                .andExpect(view().name("error"));
    }

    @Test
    void checkThatYouCanAddCategory() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        DishCategoryDTO dishCategoryDTO = dishCategoryDTO1();
        DishCategory dishCategory = dishCategory1();

        Integer restaurantId = restaurantDTO.getRestaurantId();
        String categoryName = dishCategoryDTO.getCategoryName();

        //when
        when(dishCategoryService.buildDishCategory(restaurantId.toString(), categoryName)).thenReturn(dishCategory);
        doNothing().when(dishCategoryService).addCategory(dishCategory);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_ADD_CATEGORY)
                .param("categoryName", categoryName)
                .param("restaurantId", restaurantId.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatYouCanNotAddDishWithoutAuthority() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        DishCategoryDTO dishCategoryDTO = dishCategoryDTO1();
        DishDTO dishDTO = dishDTO1();

        Integer restaurantId = restaurantDTO.getRestaurantId();
        Integer categoryId = dishCategoryDTO.getDishCategoryId();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = dishDTO.asMap();
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.putIfAbsent("categoryId", categoryId.toString());
        parametersMap.forEach(params::add);
        String expectedErrorMessage = "Error:\n[You do not have the required authority to add new dish.]";

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_ADD_DISH)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", expectedErrorMessage))
                .andExpect(view().name("error"));
    }

    @Test
    void checkThatYouCanAddDish() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        DishCategoryDTO dishCategoryDTO = dishCategoryDTO1();
        DishDTO dishDTO = dishDTO1();
        Dish dish = dish1();

        Integer restaurantId = restaurantDTO.getRestaurantId();
        Integer categoryId = dishCategoryDTO.getDishCategoryId();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = dishDTO.asMap();
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.putIfAbsent("categoryId", categoryId.toString());
        parametersMap.forEach(params::add);

        //when
        when(dishService.buildDish(dishDTO, restaurantId, categoryId)).thenReturn(dish);
        doNothing().when(dishService).addDish(dish);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_MENU_ADD_DISH)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));
    }
}