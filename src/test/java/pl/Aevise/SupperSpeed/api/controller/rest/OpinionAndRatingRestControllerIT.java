package pl.Aevise.SupperSpeed.api.controller.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import pl.Aevise.SupperSpeed.api.dto.RestOpinionDTO;
import pl.Aevise.SupperSpeed.api.dto.TotalRestaurantRatingDTO;
import pl.Aevise.SupperSpeed.integration.configuration.RestAssuredIntegrationTestBase;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.api.controller.rest.OpinionAndRatingRestController.GET_OPINIONS;
import static pl.Aevise.SupperSpeed.api.controller.rest.OpinionAndRatingRestController.GET_TOTAL_RATING;
import static pl.Aevise.SupperSpeed.api.controller.utils.URLConstants.API_UNAUTH;

class OpinionAndRatingRestControllerIT extends RestAssuredIntegrationTestBase {

    @BeforeEach
    void setupCredentials() {
        setTestCredentials("user4@user.com", "test");
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanGetAllOpinionsAboutRestaurant(
            String restaurantName,
            String sortingDirection,
            Integer page,
            Integer dishesOnPage,
            Integer expectedSize
    ) {
        //given, when
        String URL = API_UNAUTH + GET_OPINIONS;
        List<RestOpinionDTO> dishes = requestSpecificationNoAuthentication().
                pathParam("restaurantName", restaurantName)
                .queryParam("sort", sortingDirection)
                .queryParam("page", page)
                .queryParam("dishesOnPage", dishesOnPage)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .jsonPath()
                .getList(".", RestOpinionDTO.class);

        //then
        assertThat(dishes).isNotNull();
        if (expectedSize > 0) {
            assertThat(dishes).isNotEmpty();
            assertThat(dishes.get(0)).isInstanceOf(RestOpinionDTO.class);
        } else {
            assertThat(dishes).isEmpty();
        }
        assertThat(dishes).hasSize(expectedSize);
    }

    public static Stream<Arguments> checkThatYouCanGetAllOpinionsAboutRestaurant() {
        return Stream.of(
                Arguments.of("restaurant3", "asc", 0, 10, 1),
                Arguments.of("restaurant3", "asc", 100, 10, 0),
                Arguments.of("restaurant3", "desc", 0, 10, 1),
                Arguments.of("restaurant2", "desc", 0, 10, 0)
        );
    }

    @Test
    void checkThatYouCanGetRestaurantRating() {
        //given
        String restaurantName = "restaurant3";
        String URL = API_UNAUTH + GET_TOTAL_RATING;

        //when
        TotalRestaurantRatingDTO rating = requestSpecificationNoAuthentication()
                .queryParam("restaurantName", restaurantName)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(TotalRestaurantRatingDTO.class);

        //then
        assertThat(rating).isNotNull();
        assertThat(rating).isInstanceOf(TotalRestaurantRatingDTO.class);
    }
}