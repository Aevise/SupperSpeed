package pl.Aevise.SupperSpeed.api.controller.rest.authority;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.Aevise.SupperSpeed.api.dto.RestOrderDTO;
import pl.Aevise.SupperSpeed.business.SupperOrderService;
import pl.Aevise.SupperSpeed.integration.configuration.RestAssuredIntegrationTestBase;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.Aevise.SupperSpeed.api.controller.rest.authority.OrdersRestController.ORDERS;
import static pl.Aevise.SupperSpeed.api.controller.utils.URLConstants.API_AUTH_BOTH;

class OrdersRestControllerForRestaurantIT extends RestAssuredIntegrationTestBase {

    @Autowired
    private SupperOrderService supperOrderService;

    @Override
    protected void setupCredentials() {
        setTestCredentials("user3@user.com", "test");
    }

    @BeforeEach
    void setUp() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    void checkThatYouCanGetOrdersAsClient() {
        //given
        String URL = API_AUTH_BOTH + ORDERS;
        List<RestOrderDTO> expectedOrders = supperOrderService.getRestOrdersByUserEmail("user3@user.com");

        String base64Credentials = Base64.getEncoder().encodeToString("user3@user.com:test".getBytes());

        //when
        ValidatableResponse response = requestSpecificationNoAuthentication()
                .header("Authorization", "Basic " + base64Credentials)
                .header("Content-Type", "application/json")
                .get(URL)
                .then();

        List<RestOrderDTO> returnedOrders = response
                .extract()
                .jsonPath()
                .getList(".", RestOrderDTO.class);

        //then
        response.statusCode(HttpStatus.OK.value());
        assertEquals(expectedOrders.size(), returnedOrders.size());
        assertThat(expectedOrders.get(0)).isInstanceOf(RestOrderDTO.class);
    }

}