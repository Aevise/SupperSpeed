package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishCategoryJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishJpaRepository;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;
import pl.Aevise.SupperSpeed.util.Constants;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static pl.Aevise.SupperSpeed.api.controller.OpinionController.OPINION;
import static pl.Aevise.SupperSpeed.api.controller.RestaurantMenuController.RESTAURANT_MENU;

class RestaurantMenuControllerIT extends AbstractITConfiguration {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DishJpaRepository dishJpaRepository;

    @Autowired
    private DishCategoryJpaRepository dishCategoryJpaRepository;

    @Test
    void checkThatYouCanGetRestaurantMenu() {
        //given
        int restaurantId = 3;
        String restaurantName = "restaurant1";

        String url = String.format("http://localhost:%s%s/menu/%s/%s", port, basePath, restaurantId, restaurantName);

        //when
        List<DishCategoryEntity> restaurantDishCategories = dishCategoryJpaRepository.findAllByRestaurant_Id(restaurantId);
        List<DishEntity> restaurantDishes = dishJpaRepository.findAllByRestaurant_Id(restaurantId);

        ResponseEntity<String> page = testRestTemplate.getForEntity(url, String.class);
        String body = page.getBody();

        //then
        assertTrue(page.getStatusCode().is2xxSuccessful());

        assertNotNull(body);
        assertTrue(body.contains(restaurantName));
        restaurantDishCategories.forEach(dishCategory -> assertTrue(body.contains(dishCategory.getCategoryName())));
        restaurantDishes.stream()
                .filter(dish -> !dish.getIsHidden())
                .forEach(dish -> assertTrue(body.contains(dish.getName())));
    }
}