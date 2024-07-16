package pl.Aevise.SupperSpeed.api.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.UserRatingJpaRepository;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;
import pl.Aevise.SupperSpeed.integration.configuration.FlywayManualMigrationsConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static pl.Aevise.SupperSpeed.api.controller.OpinionController.OPINION;

class OpinionControllerIT extends AbstractITConfiguration {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRatingJpaRepository userRatingJpaRepository;

    @ParameterizedTest
    @MethodSource
    void showOpinionsAboutRestaurant(
            Integer restaurantId,
            String sortingDirection,
            Integer currentPage
    ) {
        //given
        String url = String.format("http://localhost:%s%s" + OPINION + "?restaurantId=%d&dir=%s&page=%d",
                port, basePath, restaurantId, sortingDirection, currentPage);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        //when
        ResponseEntity<String> response = testRestTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String body = response.getBody();

        //then
        assertTrue(response.getStatusCode().is2xxSuccessful());

        if(restaurantId.equals(3)){
            UserRatingEntity userRatingEntity = userRatingJpaRepository.findById(1).orElseThrow(
                    () -> new EntityNotFoundException("check flyway migrations"));
            List<UserRatingEntity> allRatings = userRatingJpaRepository.findAll();

            assertNotNull(body);
            assertTrue(body.contains("Total food rating: " + userRatingEntity.getFoodRating().toString().replace(".", ",")));
            assertTrue(body.contains("Total delivery rating: " + userRatingEntity.getDeliveryRating().toString().replace(".", ",")));
            assertTrue(body.contains("Total amount of rated orders: " + allRatings.size()));
            assertTrue(body.contains(userRatingEntity.getDescription()));
        }else {
            assertNotNull(body);
            assertTrue(body.contains("Total food rating: 0.0"));
            assertTrue(body.contains("Total delivery rating: 0.0"));
            assertTrue(body.contains("Total amount of rated orders: 0"));
        }
    }

    public static Stream<Arguments> showOpinionsAboutRestaurant(){
        return Stream.of(
                Arguments.of(3, "asc", 0),
                Arguments.of(2, "asc", 0),
                Arguments.of(3, "desc", 0),
                Arguments.of(2, "desc", 0)
        );
    }
}