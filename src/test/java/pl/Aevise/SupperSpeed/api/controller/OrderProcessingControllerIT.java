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
import pl.Aevise.SupperSpeed.api.dto.mapper.OffsetDateTimeMapper;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.SupperOrderJpaRepository;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;
import pl.Aevise.SupperSpeed.integration.configuration.FlywayManualMigrationsConfiguration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.OrderProcessingController.*;
import static pl.Aevise.SupperSpeed.util.Constants.*;

@AutoConfigureMockMvc
@Import(FlywayManualMigrationsConfiguration.class)
@WithMockUser(username = TEST_CLIENT_EMAIL_FLYWAY_1, password = testPassword, authorities = "CLIENT")
class OrderProcessingControllerIT extends AbstractITConfiguration {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    Flyway flyway;

    @Autowired
    private DishJpaRepository dishJpaRepository;

    @Autowired
    private SupperOrderJpaRepository supperOrderJpaRepository;

    @Autowired
    private OffsetDateTimeMapper offsetDateTimeMapper;

    @BeforeEach
    void recreateFlywayMigrations() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void getRestaurantMenu() throws Exception {
        //given
        int restaurantId = 3;
        int dishId = 1;
        String restaurantName = "restaurant3";
        String amountOfDish = "2";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("restaurantId", Integer.toString(restaurantId));
        parametersMap.putIfAbsent("restaurantName", restaurantName);
        parametersMap.putIfAbsent(String.format("amountOfDishWithId_%s", dishId), amountOfDish);
        parametersMap.forEach(params::add);

        //when
        DishEntity dishEntity = dishJpaRepository.findById(dishId).orElseThrow(
                () -> new EntityNotFoundException("Check flyway migrations"));

        ResultActions result = mockMvc.perform(post(ORDER_PROCESSING)
                .params(params));
        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("order_processing"));

        assertThat(content).contains("Thank you for your order");
        assertThat(content).contains(dishEntity.getName());
        assertThat(content).contains(String.format("%s x %s", amountOfDish, dishEntity.getPrice().toString()));
        assertThat(content).contains("Total value: " + dishEntity.getPrice().multiply(BigDecimal.valueOf(Double.parseDouble(amountOfDish))).setScale(2, RoundingMode.UP));
    }

    @ParameterizedTest
    @MethodSource
    void checkThatOrderPaymentWorksCorrectly(
            int orderId,
            int expectedOrderStatus
    ) throws Exception {
        //given, when
        SupperOrderEntity oldSupperOrder = supperOrderJpaRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));

        ResultActions result = mockMvc.perform(post(ORDER_PAYMENT)
                .param("orderId", String.valueOf(orderId)));

        SupperOrderEntity newSupperOrder = supperOrderJpaRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/orders"));

        if (orderId == 1) {
            assertNotEquals(oldSupperOrder.getStatus().getStatusId(), newSupperOrder.getStatus().getStatusId());
        } else {
            assertEquals(oldSupperOrder.getStatus().getStatusId(), newSupperOrder.getStatus().getStatusId());
        }

        assertEquals(expectedOrderStatus, newSupperOrder.getStatus().getStatusId());
    }

    public static Stream<Arguments> checkThatOrderPaymentWorksCorrectly() {
        return Stream.of(
                Arguments.of(1, 2),
                Arguments.of(2, 2),
                Arguments.of(3, 3)
        );
    }

    @ParameterizedTest
    @MethodSource
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, password = testPassword, authorities = "RESTAURANT")
    void checkThatRestaurantCanProceedOrder(
            int orderId,
            int expectedOrderStatus,
            boolean shouldChangeStatus
    ) throws Exception {
        //given, when
        SupperOrderEntity oldSupperOrder = supperOrderJpaRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));

        ResultActions result = mockMvc.perform(post(PROCEED_ORDER)
                .param("orderId", String.valueOf(orderId)));

        SupperOrderEntity newSupperOrder = supperOrderJpaRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/orders"));

        if (shouldChangeStatus) {
            assertNotEquals(oldSupperOrder.getStatus().getStatusId(), newSupperOrder.getStatus().getStatusId());
        } else {
            assertEquals(oldSupperOrder.getStatus().getStatusId(), newSupperOrder.getStatus().getStatusId());
        }

        assertEquals(expectedOrderStatus, newSupperOrder.getStatus().getStatusId());
    }

    public static Stream<Arguments> checkThatRestaurantCanProceedOrder() {
        return Stream.of(
                Arguments.of(1, 1, false),
                Arguments.of(2, 3, true),
                Arguments.of(3, 4, true),
                Arguments.of(4, 5, true),
                Arguments.of(5, 5, false),
                Arguments.of(6, 6, false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanCancelOrderWithCorrectTime(
            int orderId,
            int currentOrderStatus,
            int expectedOrderStatus,
            boolean shouldChangeStatus
    ) throws Exception {
        //given, when
        SupperOrderEntity oldSupperOrder = supperOrderJpaRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));

        ResultActions result = mockMvc.perform(post(CANCEL_ORDER)
                .param("orderId", String.valueOf(orderId))
                .param("statusId", String.valueOf(currentOrderStatus))
                .param("orderDate", offsetDateTimeMapper.mapOffsetDateTimeToString(oldSupperOrder.getOrderDateTime())));

        SupperOrderEntity newSupperOrder = supperOrderJpaRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order not existing"));

        //then

        if (shouldChangeStatus) {
            assertNotEquals(oldSupperOrder.getStatus().getStatusId(), newSupperOrder.getStatus().getStatusId());
            result
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(view().name("redirect:/orders"));
        } else {
            assertEquals(oldSupperOrder.getStatus().getStatusId(), newSupperOrder.getStatus().getStatusId());
            result
                    .andExpect(status().isNotAcceptable())
                    .andExpect(model().attributeExists("errorMessage"))
                    .andExpect(model().attribute("errorMessage", "Error:\n[Order with this status can not be cancelled]"))
                    .andExpect(view().name("error"));
        }

        assertEquals(expectedOrderStatus, newSupperOrder.getStatus().getStatusId());
    }

    public static Stream<Arguments> checkThatYouCanCancelOrderWithCorrectTime() {
        return Stream.of(
                Arguments.of(1, 1, 6, true),
                Arguments.of(2, 2, 6, true),
                Arguments.of(3, 3, 3, false),
                Arguments.of(4, 4, 4, false),
                Arguments.of(5, 5, 5, false),
                Arguments.of(6, 6, 6, false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanNotCancelOrderWithCorrectStatusButIncorrectTime(
            int orderId,
            int currentOrderStatus,
            int expectedOrderStatus,
            boolean shouldChangeStatus,
            int minusMinutes
    ) throws Exception {
        //given, when
        SupperOrderEntity oldSupperOrder = supperOrderJpaRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Check Flyway migrations"));
        oldSupperOrder.setOrderDateTime(oldSupperOrder.getOrderDateTime().minusMinutes(minusMinutes));

        ResultActions result = mockMvc.perform(post(CANCEL_ORDER)
                .param("orderId", String.valueOf(orderId))
                .param("statusId", String.valueOf(currentOrderStatus))
                .param("orderDate", offsetDateTimeMapper.mapOffsetDateTimeToString(oldSupperOrder.getOrderDateTime())));

        SupperOrderEntity newSupperOrder = supperOrderJpaRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order not existing"));

        //then

        if (shouldChangeStatus) {
            assertNotEquals(oldSupperOrder.getStatus().getStatusId(), newSupperOrder.getStatus().getStatusId());
            result
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(view().name("redirect:/orders"));
        } else {
            assertEquals(oldSupperOrder.getStatus().getStatusId(), newSupperOrder.getStatus().getStatusId());
            result
                    .andExpect(status().isNotAcceptable())
                    .andExpect(model().attributeExists("errorMessage"))
                    .andExpect(model().attribute("errorMessage", "Error:\n[More than 20 minutes passed. Order can not be cancelled]"))
                    .andExpect(view().name("error"));
        }

        assertEquals(expectedOrderStatus, newSupperOrder.getStatus().getStatusId());
    }

    public static Stream<Arguments> checkThatYouCanNotCancelOrderWithCorrectStatusButIncorrectTime() {
        return Stream.of(
                Arguments.of(2, 2, 6, true, 19),
                Arguments.of(2, 2, 2, false, 21)
        );
    }
}