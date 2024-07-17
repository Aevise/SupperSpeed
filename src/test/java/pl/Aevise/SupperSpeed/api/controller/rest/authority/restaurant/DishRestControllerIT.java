package pl.Aevise.SupperSpeed.api.controller.rest.authority.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import jakarta.persistence.EntityNotFoundException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishJpaRepository;
import pl.Aevise.SupperSpeed.integration.configuration.RestAssuredIntegrationTestBase;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static pl.Aevise.SupperSpeed.api.controller.rest.authority.restaurant.DishRestController.*;
import static pl.Aevise.SupperSpeed.api.controller.utils.URLConstants.API_AUTH_RESTAURANT;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.dishDTO1;

class DishRestControllerIT extends RestAssuredIntegrationTestBase {

    @Autowired
    private DishJpaRepository dishJpaRepository;

    @Autowired
    private Flyway flyway;

    @Override
    protected void setupCredentials() {
        setTestCredentials("user3@user.com", "test");
    }

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterEach
    void recreateFlywayMigrations() {
        flyway.clean();
        flyway.migrate();
    }


    @Test
    void checkThatYouCanAddNewDish() {
        //given
        String URL = API_AUTH_RESTAURANT + ADD_DISH;
        DishDTO dishDTO = dishDTO1();
        dishDTO.setDishId(null);
        dishDTO.setCategory("Dania rybne");
        dishDTO.setName("dish does not exist");

        String base64Credentials = Base64.getEncoder().encodeToString("user3@user.com:test".getBytes());

        //when
        ValidatableResponse response = requestSpecificationNoAuthentication()
                .header("Authorization", "Basic " + base64Credentials)
                .header("Content-Type", "application/json")
                .body(dishDTO)
                .post(URL)
                .then();
        DishDTO returnedDish = response.extract().as(DishDTO.class);
        Optional<DishEntity> newDish = dishJpaRepository.findByName(returnedDish.getName());

        //then
        response.statusCode(HttpStatus.CREATED.value());
        assertNotNull(returnedDish);
        assertTrue(newDish.isPresent());
        assertThat(newDish.get().getName()).isEqualTo(dishDTO.getName());
        assertThat(newDish.get().getDescription()).isEqualTo(dishDTO.getDescription());
    }

    @Test
    void checkThatYouCanUpdateExistingDish() {
        //given
        String URL = API_AUTH_RESTAURANT + UPDATE_DISH;
        DishDTO dishDTO = dishDTO1();
        Integer dishId = dishDTO.getDishId();

        String base64Credentials = Base64.getEncoder().encodeToString("user3@user.com:test".getBytes());

        //when
        ValidatableResponse response = requestSpecificationNoAuthentication()
                .header("Authorization", "Basic " + base64Credentials)
                .header("Content-Type", "application/json")
                .queryParam("dishId", dishId)
                .body(dishDTO)
                .put(URL)
                .then();
        DishDTO returnedDish = response.extract().as(DishDTO.class);
        Optional<DishEntity> newDish = dishJpaRepository.findById(dishId);

        //then
        response.statusCode(HttpStatus.OK.value());
        assertNotNull(returnedDish);
        assertTrue(newDish.isPresent());
        assertThat(newDish.get().getName()).isEqualTo(dishDTO.getName());
        assertThat(newDish.get().getDescription()).isEqualTo(dishDTO.getDescription());
        assertThat(newDish.get().getDescription()).isEqualTo(dishDTO.getDescription());
    }

    @Test
    void checkThatRequestIsDeniedWhenTryingToChangeNotYourDish() {
        //given
        String URL = API_AUTH_RESTAURANT + UPDATE_DISH;
        DishDTO dishDTO = dishDTO1();
        dishDTO.setDishId(null);
        Integer dishId = 8;
        String expectedError = "Error:\n[Can not modify this dish]";

        String base64Credentials = Base64.getEncoder().encodeToString("user3@user.com:test".getBytes());

        //when
        ValidatableResponse response = requestSpecificationNoAuthentication()
                .header("Authorization", "Basic " + base64Credentials)
                .header("Content-Type", "application/json")
                .queryParam("dishId", dishId)
                .body(dishDTO)
                .put(URL)
                .then();
        String responseBody = response.extract().asString();

        //then
        response.statusCode(HttpStatus.BAD_REQUEST.value());
        assertThat(responseBody).isEqualTo(expectedError);
    }

    @ParameterizedTest
    @MethodSource
    void checkThatRequestIsDeniedWhenTryingToPassIncompleteDishInformation(
            String dishName,
            String description,
            BigDecimal price,
            String expectedErrorMessage
    ) {
        //given
        String URL = API_AUTH_RESTAURANT + UPDATE_DISH;
        DishDTO dishDTO = dishDTO1();
        dishDTO.setName(dishName);
        dishDTO.setPrice(price);
        dishDTO.setDescription(description);
        Integer dishId = dishDTO.getDishId();

        String base64Credentials = Base64.getEncoder().encodeToString("user3@user.com:test".getBytes());

        //when
        ValidatableResponse response = requestSpecificationNoAuthentication()
                .header("Authorization", "Basic " + base64Credentials)
                .header("Content-Type", "application/json")
                .queryParam("dishId", dishId)
                .body(dishDTO)
                .put(URL)
                .then();
        String responseBody = response.extract().asString();

        //then
        response.statusCode(HttpStatus.BAD_REQUEST.value());
        assertThat(responseBody).isEqualTo(expectedErrorMessage);
    }

    public static Stream<Arguments> checkThatRequestIsDeniedWhenTryingToPassIncompleteDishInformation() {
        return Stream.of(
                Arguments.of(null, "desc", BigDecimal.ONE, "Error:\n[Body must contain name value]"),
                Arguments.of("name", null, BigDecimal.ONE, "Error:\n[Body must contain description value]"),
                Arguments.of("name", "desc", null, "Error:\n[Body must contain price value]")
        );
    }

    @Test
    void checkThatYouCanCorrectlyDeleteDish() {
        //given
        String URL = API_AUTH_RESTAURANT + DELETE_DISH;
        Integer dishId = 4;

        String base64Credentials = Base64.getEncoder().encodeToString("user3@user.com:test".getBytes());

        Optional<DishEntity> oldDish = dishJpaRepository.findById(dishId);

        //when
        ValidatableResponse response = requestSpecificationNoAuthentication()
                .header("Authorization", "Basic " + base64Credentials)
                .header("Content-Type", "application/json")
                .queryParam("dishId", dishId)
                .delete(URL)
                .then();

        Optional<DishEntity> dishAfterRequest = dishJpaRepository.findById(dishId);

        //then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        assertTrue(oldDish.isPresent());
        assertTrue(dishAfterRequest.isEmpty());
    }

    @Test
    void checkThatDishIsHiddenIfOrderedAtLeastOnce() {
        //given
        String URL = API_AUTH_RESTAURANT + DELETE_DISH;
        Integer dishId = 1;

        String base64Credentials = Base64.getEncoder().encodeToString("user3@user.com:test".getBytes());

        Optional<DishEntity> oldDish = dishJpaRepository.findById(dishId);

        //when
        ValidatableResponse response = requestSpecificationNoAuthentication()
                .header("Authorization", "Basic " + base64Credentials)
                .header("Content-Type", "application/json")
                .queryParam("dishId", dishId)
                .delete(URL)
                .then();

        Optional<DishEntity> dishAfterRequest = dishJpaRepository.findById(dishId);

        //then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        assertTrue(oldDish.isPresent());
        assertFalse(oldDish.get().getIsHidden());

        assertTrue(dishAfterRequest.isPresent());
        assertTrue(dishAfterRequest.get().getIsHidden());
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanNotDeleteDishWithWrongRequest(
            Integer dishId
    ) {
        //given
        String URL = API_AUTH_RESTAURANT + DELETE_DISH;
        String expectedError = "Error:\n[You can not delete this dish]";

        String base64Credentials = Base64.getEncoder().encodeToString("user3@user.com:test".getBytes());

        //when
        ValidatableResponse response = requestSpecificationNoAuthentication()
                .header("Authorization", "Basic " + base64Credentials)
                .header("Content-Type", "application/json")
                .queryParam("dishId", dishId)
                .delete(URL)
                .then();
        String responseBody = response.extract().asString();

        //then
        response.statusCode(HttpStatus.BAD_REQUEST.value());
        assertThat(responseBody).isEqualTo(expectedError);
    }

    public static Stream<Integer> checkThatYouCanNotDeleteDishWithWrongRequest() {
        return Stream.of(999, 8);
    }

    @Test
    void checkThatYouCanNotDeleteHiddenDish() {
        //given
        String URL = API_AUTH_RESTAURANT + DELETE_DISH;
        String expectedError = "Error:\n[You can not delete this dish]";
        int dishId = 1;

        String base64Credentials = Base64.getEncoder().encodeToString("user3@user.com:test".getBytes());

        DishEntity oldDish = dishJpaRepository.findById(dishId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));
        oldDish.setIsHidden(true);
        dishJpaRepository.saveAndFlush(oldDish);

        //when
        ValidatableResponse response = requestSpecificationNoAuthentication()
                .header("Authorization", "Basic " + base64Credentials)
                .header("Content-Type", "application/json")
                .queryParam("dishId", dishId)
                .delete(URL)
                .then();
        String responseBody = response.extract().asString();

        DishEntity newDish = dishJpaRepository.findById(dishId).orElseThrow(
                () -> new EntityNotFoundException("Dish deleted?"));

        //then
        response.statusCode(HttpStatus.BAD_REQUEST.value());
        assertThat(responseBody).isEqualTo(expectedError);
        assertThat(newDish).isEqualTo(oldDish);
    }
}