package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import pl.Aevise.SupperSpeed.util.DTOFixtures;
import pl.Aevise.SupperSpeed.util.POJOFixtures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.RestaurantMenuController.RESTAURANT_MENU;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.*;

@WebMvcTest(controllers = RestaurantMenuController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
class RestaurantMenuControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private DishService dishService;
    @MockBean
    private DishListService dishListService;
    @MockBean
    private ImageHandlingService imageHandlingService;
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private RestaurantMapper restaurantMapper;
    @MockBean
    private SecurityService securityService;

    @Test
    @WithMockUser
    void checkThatExceptionIsThrownWhenWrongRestaurantIdIsGiven() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        int restaurantId = -1;
        String restaurantName = restaurantDTO.getRestaurantName();
        String URL = "/menu/" + restaurantId + "/" + restaurantName;

        String expectedErrorMessage = "Error:\n[Wrong identification provided. User Not Found.]";

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", expectedErrorMessage))
                .andExpect(view().name("error"));
    }

    @Test
    void checkThatYouGetNotificationThatRestaurantDoesNotHaveActiveMenu() throws Exception {
        //given
        Restaurant restaurant = restaurant1();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Integer restaurantId = restaurantDTO.getRestaurantId();
        String restaurantName = restaurantDTO.getRestaurantName();
        String restaurantDirectory = String.format(restaurantId + "_" + restaurantName);

        List<DishCategoryDTO> dishCategoryDTOS = List.of();
        String URL = "/menu/" + restaurantId + "/" + restaurantName;
        String authority = "RESTAURANT";
        String expectedPartOfHTML = "<h1>Sorry, restaurant does not have active menu:</h1>";

        //when
        Mockito.when(dishListService.getDishCategoriesByRestaurantId(restaurantId)).thenReturn(dishCategoryDTOS);
        Mockito.when(restaurantMapper.mapToDTO(restaurant)).thenReturn(restaurantDTO);
        Mockito.when(restaurantService.findRestaurantById(restaurantId)).thenReturn(restaurant);
        Mockito.when(imageHandlingService.getRestaurantName(restaurantId, restaurantName)).thenReturn(restaurantDirectory);
        Mockito.when(securityService.getUserAuthority()).thenReturn(authority);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("restaurantId"))
                .andExpect(model().attribute("restaurantId", restaurantId))
                .andExpect(model().attributeExists("restaurantDirectory"))
                .andExpect(model().attribute("restaurantDirectory", restaurantDirectory))
                .andExpect(model().attributeExists("restaurantName"))
                .andExpect(model().attribute("restaurantName", restaurantName))
                .andExpect(model().attributeExists("addressDTO"))
                .andExpect(model().attribute("addressDTO", restaurantDTO.getAddress()))
                .andExpect(model().attributeExists("userRole"))
                .andExpect(model().attribute("userRole", authority))
                .andExpect(view().name("restaurant_menu"));

        assertThat(content).contains(expectedPartOfHTML);
    }

    @Test
    void checkThatYouGetRestaurantActiveMenu() throws Exception {
        //given
        Restaurant restaurant = restaurant1();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Integer restaurantId = restaurantDTO.getRestaurantId();
        String restaurantName = restaurantDTO.getRestaurantName();
        String restaurantDirectory = String.format(restaurantId + "_" + restaurantName);
        String authority = "RESTAURANT";
        DishDTO dishDTO = dishDTO1();

        List<DishCategoryDTO> dishCategoryDTOS = List.of(dishCategoryDTO1(), dishCategoryDTO2());
        String URL = "/menu/" + restaurantId + "/" + restaurantName;
        HashMap<List<DishCategoryDTO>, List<DishDTO>> dishMap = new HashMap<>();
        dishMap.putIfAbsent(List.of(dishCategoryDTO1()), List.of(dishDTO));
        List<String> HTMLShouldContain = List.of(
                "<td>" + dishDTO.getName() + "</td>",
                "<td>" + dishDTO.getDescription() + "</td>",
                "<td>" + dishDTO.getPrice() + "</td>"
        );

        //when
        Mockito.when(dishListService.getDishCategoriesByRestaurantId(restaurantId)).thenReturn(dishCategoryDTOS);
        Mockito.when(dishService.extractDishesByCategory(dishCategoryDTOS, true)).thenReturn(dishMap);
        Mockito.when(restaurantMapper.mapToDTO(restaurant)).thenReturn(restaurantDTO);
        Mockito.when(restaurantService.findRestaurantById(restaurantId)).thenReturn(restaurant);
        Mockito.when(imageHandlingService.getRestaurantName(restaurantId, restaurantName)).thenReturn(restaurantDirectory);
        Mockito.when(securityService.getUserAuthority()).thenReturn(authority);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("restaurantId"))
                .andExpect(model().attribute("restaurantId", restaurantId))
                .andExpect(model().attributeExists("dishesByCategory"))
                .andExpect(model().attribute("dishesByCategory", dishMap))
                .andExpect(model().attributeExists("restaurantDirectory"))
                .andExpect(model().attribute("restaurantDirectory", restaurantDirectory))
                .andExpect(model().attributeExists("restaurantName"))
                .andExpect(model().attribute("restaurantName", restaurantName))
                .andExpect(model().attributeExists("addressDTO"))
                .andExpect(model().attribute("addressDTO", restaurantDTO.getAddress()))
                .andExpect(model().attributeExists("userRole"))
                .andExpect(model().attribute("userRole", authority))
                .andExpect(view().name("restaurant_menu"));

        assertThat(content).contains(HTMLShouldContain);
    }
}