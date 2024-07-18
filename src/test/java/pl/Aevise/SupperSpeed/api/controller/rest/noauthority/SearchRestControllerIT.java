package pl.Aevise.SupperSpeed.api.controller.rest.noauthority;

import io.restassured.response.ValidatableResponse;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.Aevise.SupperSpeed.api.dto.RestRestaurantDTO;
import pl.Aevise.SupperSpeed.integration.configuration.RestAssuredIntegrationTestBase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.api.controller.rest.noAuthority.SearchRestController.SEARCH_ENDPOINT;
import static pl.Aevise.SupperSpeed.api.controller.utils.URLConstants.API_UNAUTH;

class SearchRestControllerIT extends RestAssuredIntegrationTestBase {

    @Autowired
    private Flyway flyway;

    @Override
    protected void setupCredentials() {
        setTestCredentials("user4@user.com", "test");
    }

    @AfterEach
    void recreateFlywayMigrations() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void checkThatYouCanSearchForRestaurantsInCity() {
        //given/
        String URL = API_UNAUTH + SEARCH_ENDPOINT;
        String city = "Lublin";
        int expectedSize = 2;

        //when
        List<RestRestaurantDTO> dishes = requestSpecificationNoAuthentication().
                queryParam("city", city)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .jsonPath()
                .getList(".", RestRestaurantDTO.class);

        //then
        assertThat(dishes).isNotNull();
        assertThat(dishes).isNotEmpty();
        assertThat(dishes.get(0)).isInstanceOf(RestRestaurantDTO.class);

        assertThat(dishes).hasSize(expectedSize);
    }

    @Test
    void checkThatYouCanSearchForRestaurantsWithCuisineInCity() {
        //given/
        String URL = API_UNAUTH + SEARCH_ENDPOINT;
        String city = "Lublin";
        String cuisine = "Italian";
        int expectedSize = 1;

        //when
        List<RestRestaurantDTO> dishes = requestSpecificationNoAuthentication()
                .queryParam("city", city)
                .queryParam("cuisine", cuisine)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .jsonPath()
                .getList(".", RestRestaurantDTO.class);

        //then
        assertThat(dishes).isNotNull();
        assertThat(dishes).isNotEmpty();
        assertThat(dishes.get(0)).isInstanceOf(RestRestaurantDTO.class);

        assertThat(dishes).hasSize(expectedSize);
    }

    @Test
    void checkThatNotFoundIsSentWhenCityHasNoVisibleRestaurants() {
        //given/
        String URL = API_UNAUTH + SEARCH_ENDPOINT;
        String city = "Chelm";

        //when
        ValidatableResponse response = requestSpecificationNoAuthentication().
                queryParam("city", city)
                .get(URL)
                .then();

        //then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void checkThatBadRequestIsSentWhenYouTryToReachEndpointWithoutRequiredParams() {
        //given/
        String URL = API_UNAUTH + SEARCH_ENDPOINT;
        String expectedError = "Error:\n[Required request parameter 'city' for method parameter type String is not present]";

        //when
        ValidatableResponse response = requestSpecificationNoAuthentication()
                .get(URL)
                .then();
        String responseBody = response.extract().asString();

        //then
        response.statusCode(HttpStatus.BAD_REQUEST.value());
        assertThat(responseBody).isEqualTo(expectedError);
    }
}