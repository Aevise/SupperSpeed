package pl.Aevise.SupperSpeed.api.controller.rest.noauthority;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.integration.configuration.RestAssuredIntegrationTestBase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.api.controller.rest.noAuthority.DishesSearchRestController.ALL_DISHES_FROM_RESTAURANT;
import static pl.Aevise.SupperSpeed.api.controller.rest.noAuthority.DishesSearchRestController.ALL_DISHES_FROM_RESTAURANT_BY_CATEGORY;
import static pl.Aevise.SupperSpeed.api.controller.utils.URLConstants.API_UNAUTH;

class DishesSearchRestControllerIT extends RestAssuredIntegrationTestBase {

    @Override
    protected void setupCredentials() {
        setTestCredentials("user4@user.com", "test");
    }

    @Test
    void allDishesFromRestaurant() {
        //given
        String restaurantName = "restaurant3";

        //when
        String URL = API_UNAUTH + ALL_DISHES_FROM_RESTAURANT;
        List<DishDTO> dishes = requestSpecificationNoAuthentication().
                pathParam("restaurantName", restaurantName)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .jsonPath()
                .getList(".", DishDTO.class);

        //then
        assertThat(dishes).isNotNull();
        assertThat(dishes).isNotEmpty();
        assertThat(dishes.get(0)).isInstanceOf(DishDTO.class);
        assertThat(dishes).hasSize(5);
    }


    @Test
    void dishesFromRestaurantByCategory() {
        //given
        String restaurantName = "restaurant3";
        String dishCategory = "Dania rybne";

        //when
        String URL = API_UNAUTH + ALL_DISHES_FROM_RESTAURANT_BY_CATEGORY;
        List<DishDTO> dishes = requestSpecificationNoAuthentication()
                .pathParam("restaurantName", restaurantName)
                .pathParam("category", dishCategory)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .jsonPath()
                .getList(".", DishDTO.class);

        //then
        assertThat(dishes).isNotNull();
        assertThat(dishes).isNotEmpty();
        assertThat(dishes.get(0)).isInstanceOf(DishDTO.class);
        assertThat(dishes).hasSize(2);
    }
}