package pl.Aevise.SupperSpeed.api.controller;

import jakarta.persistence.EntityNotFoundException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishCategoryJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishJpaRepository;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;
import pl.Aevise.SupperSpeed.integration.configuration.FlywayManualMigrationsConfiguration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.RestaurantMenuEditionController.*;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_FLYWAY_1;
import static pl.Aevise.SupperSpeed.util.Constants.testPassword;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.dishDTO1;

@AutoConfigureMockMvc
@Import(FlywayManualMigrationsConfiguration.class)
@WithMockUser(username = TEST_RESTAURANT_EMAIL_FLYWAY_1, password = testPassword, authorities = "RESTAURANT")
class RestaurantMenuEditionControllerIT extends AbstractITConfiguration {

    @Autowired
    Flyway flyway;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DishCategoryJpaRepository dishCategoryJpaRepository;

    @Autowired
    private DishJpaRepository dishJpaRepository;

    public static Stream<Arguments> checkThatYouCanSuccessfullyDeleteDish() {
        return Stream.of(
                Arguments.of(true, 1),
                Arguments.of(false, 5)
        );
    }

    @BeforeEach
    void recreateFlywayMigrations() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void checkThatYouCanGetRestaurantMenu() throws Exception {
        //given
        int restaurantId = 3;

        // when
        List<DishCategoryEntity> restaurantDishCategories = dishCategoryJpaRepository.findAllByRestaurant_Id(restaurantId);
        List<DishEntity> restaurantDishes = dishJpaRepository.findAllByRestaurant_Id(restaurantId);

        ResultActions result = mockMvc.perform(get(RESTAURANT_MENU_EDIT));
        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dishesByCategory"))
                .andExpect(model().attributeExists("restaurantId"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("restaurantDirectory"))
                .andExpect(model().attributeExists("imageWidth"))
                .andExpect(model().attributeExists("imageHeight"))
                .andExpect(model().attributeExists("restaurantName"))
                .andExpect(view().name("restaurant_menu_edit"));

        restaurantDishCategories.forEach(dishCategory -> assertTrue(content.contains(dishCategory.getCategoryName())));
        restaurantDishes.forEach(dish -> assertTrue(content.contains(dish.getName())));
    }

    @Test
    void checkThatYouCanUpdateDish() throws Exception {
        //given
        DishEntity oldDish = dishJpaRepository.findById(1).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));
        String newName = "changed Name";
        String newDescription = "changes";

        DishDTO newDishInformation = DishDTO.builder()
                .dishId(oldDish.getDishId())
                .name(newName)
                .description(newDescription)
                .price(BigDecimal.TEN)
                .availability(null)
                .isHidden(false)
                .build();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = newDishInformation.asMap();
        parametersMap.forEach(params::add);

        //when
        ResultActions result = mockMvc.perform(post(RESTAURANT_MENU_UPDATE_DISH)
                .params(params));

        DishEntity newDish = dishJpaRepository.findById(1).orElseThrow(
                () -> new EntityNotFoundException("Dish deleted?"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));

        assertFalse(newDish.getAvailability());
        assertNotEquals(oldDish.getDescription(), newDish.getDescription());
        assertNotEquals(oldDish.getName(), newDish.getName());
        assertEquals(oldDish.getDishId(), newDish.getDishId());
        assertEquals(newDish.getDescription(), newDescription);
        assertEquals(newDish.getName(), newName);
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanSuccessfullyDeleteDish(
            boolean dishInOrder,
            int dishId
    ) throws Exception {
        //given
        DishEntity oldDish = dishJpaRepository.findById(dishId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));

        //when
        ResultActions result = mockMvc.perform(post(RESTAURANT_MENU_DELETE_DISH)
                .param("dishId", String.valueOf(dishId)));

        Optional<DishEntity> dish = dishJpaRepository.findById(dishId);

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));

        if (dishInOrder) {
            assertTrue(dish.isPresent());
            assertTrue(dish.get().getIsHidden());
            assertEquals(oldDish.getDishId(), dish.get().getDishId());
        } else {
            assertTrue(dish.isEmpty());
        }
    }

    @Test
    void checkThatYouCanDeleteDishCategory() throws Exception {
        //given
        int dishCategoryId = 2;

        List<DishEntity> oldDishesInCategory = dishJpaRepository.findAllByDishCategory_DishCategoryId(2);
        //when
        ResultActions result = mockMvc.perform(post(RESTAURANT_MENU_DELETE_CATEGORY)
                .param("dishCategoryId", String.valueOf(dishCategoryId)));

        Optional<DishCategoryEntity> dishCategoryShouldBeEmpty = dishCategoryJpaRepository.findById(dishCategoryId);

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));

        assertEquals(2, oldDishesInCategory.size());
        assertTrue(dishCategoryShouldBeEmpty.isEmpty());
        for (DishEntity dishEntity : oldDishesInCategory) {
            Optional<DishEntity> dish = dishJpaRepository.findById(dishEntity.getDishId());
            dish.ifPresent(entity -> assertTrue(entity.getIsHidden()));
        }
    }

    @Test
    void checkThatYouCanUpdateDishCategoryName() throws Exception {
        //given
        String newCategoryName = "changing dish category name";
        int dishCategoryId = 1;
        DishCategoryEntity oldDishCategory = dishCategoryJpaRepository.findById(dishCategoryId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));

        //when
        ResultActions result = mockMvc.perform(post(RESTAURANT_MENU_UPDATE_CATEGORY)
                .param("dishCategoryId", String.valueOf(dishCategoryId))
                .param("categoryName", newCategoryName));

        DishCategoryEntity newDishCategory = dishCategoryJpaRepository.findById(dishCategoryId).orElseThrow(
                () -> new EntityNotFoundException("Dish category deleted?"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));

        assertEquals(oldDishCategory.getDishCategoryId(), newDishCategory.getDishCategoryId());
        assertEquals(newDishCategory.getCategoryName(), newCategoryName);
    }

    @Test
    void checkThatYouCanAddDishCategory() throws Exception {
        //given
        int restaurantId = 1;
        String newCategoryName = "new test category";
        int expectedDishCategoryId = 5;

        //when
        ResultActions result = mockMvc.perform(post(RESTAURANT_MENU_ADD_CATEGORY)
                .param("restaurantId", String.valueOf(restaurantId))
                .param("categoryName", newCategoryName));

        DishCategoryEntity newDishCategory = dishCategoryJpaRepository.findById(expectedDishCategoryId).orElseThrow(
                () -> new EntityNotFoundException("Dish category not added"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));

        assertEquals(restaurantId, newDishCategory.getRestaurant().getId());
        assertEquals(newCategoryName, newDishCategory.getCategoryName());
    }

    @Test
    void checkThatYouCanAddDish() throws Exception {
        //given
        int restaurantId = 1;
        int categoryId = 1;
        DishDTO dishDTO = dishDTO1();
        dishDTO.setDishId(null);
        int expectedDishId = 6;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = dishDTO.asMap();
        parametersMap.putIfAbsent("restaurantId", String.valueOf(restaurantId));
        parametersMap.putIfAbsent("categoryId", String.valueOf(categoryId));
        parametersMap.forEach(params::add);

        //when
        ResultActions result = mockMvc.perform(post(RESTAURANT_MENU_ADD_DISH)
                .params(params));

        DishEntity newDish = dishJpaRepository.findById(expectedDishId).orElseThrow(
                () -> new EntityNotFoundException("Dish not added"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));

        assertEquals(dishDTO.getDescription(), newDish.getDescription());
        assertEquals(dishDTO.getName(), newDish.getName());
        assertEquals(dishDTO.getAvailability(), newDish.getAvailability());
        assertEquals(dishDTO.getIsHidden(), newDish.getIsHidden());
    }
}