package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.api.controller.utils.OrderStatus;
import pl.Aevise.SupperSpeed.api.dto.DishListDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.OffsetDateTimeMapper;
import pl.Aevise.SupperSpeed.business.DishListService;
import pl.Aevise.SupperSpeed.business.SupperOrderService;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.OrderProcessingController.*;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.dishListDTO1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.restaurantDTO1;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.*;

@WebMvcTest(controllers = OrderProcessingController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
class OrderProcessingControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private SupperOrderService supperOrderService;
    @MockBean
    private DishListService dishListService;
    @MockBean
    private OffsetDateTimeMapper offsetDateTimeMapper;
    @MockBean
    private SecurityService securityService;

    @Test
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    void checkThatYouCanNotPlaceOrderAsRestaurantClient() throws Exception {
        //given
        String authority = AvailableRoles.RESTAURANT.toString();
        RestaurantDTO restaurantDTO = restaurantDTO1();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("restaurantId", restaurantDTO.getRestaurantId().toString());
        parametersMap.putIfAbsent("restaurantName", restaurantDTO.getRestaurantName());
        parametersMap.forEach(params::add);

        //when
        when(securityService.getUserAuthority()).thenReturn(authority);

        ResultActions result = mockMvc.perform(post(ORDER_PROCESSING)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/orders"));
    }

    @Test
    void checkThatExceptionIsThrownWhenNoDishesAreFound() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("restaurantId", restaurantDTO.getRestaurantId().toString());
        parametersMap.putIfAbsent("restaurantName", restaurantDTO.getRestaurantName());
        parametersMap.forEach(params::add);

        //when
        when(securityService.getUserAuthority()).thenReturn("CLIENT");

        ResultActions result = mockMvc.perform(post(ORDER_PROCESSING)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "Could not find a resource: [Restaurant menu]"))
                .andExpect(view().name("error"));
    }

    @Test
    void checkThatOrderCanBePlacedCorrectly() throws Exception {
        //given
        String authority = AvailableRoles.CLIENT.toString();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Integer restaurantId = restaurantDTO.getRestaurantId();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.putIfAbsent("restaurantName", restaurantDTO.getRestaurantName());
        parametersMap.putIfAbsent("amountOfDishWithId_1", "1");
        parametersMap.forEach(params::add);

        SupperOrderEntity orderEntity = buildSupperOrderEntity(restaurantEntity1(), clientEntity1());
        orderEntity.setOrderId(1);
        Map<Integer, Integer> dishIdAndQuantities = Map.of(1, 1);
        List<DishListDTO> dishesDTO = List.of(dishListDTO1());
        BigDecimal orderValue = BigDecimal.TEN;

        // when
        when(securityService.getUserAuthority()).thenReturn(authority);
        when(supperOrderService.createNewOrder(restaurantId, TEST_CLIENT_EMAIL_1))
                .thenReturn(orderEntity);
        when(dishListService.saveAllByOrderAndDishQuantity(1, dishIdAndQuantities)).thenReturn(dishesDTO);
        when(supperOrderService.extractTotalOrderValue(dishesDTO)).thenReturn(orderValue);

        ResultActions result = mockMvc.perform(post(ORDER_PROCESSING)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("restaurantId"))
                .andExpect(model().attribute("restaurantId", restaurantId))
                .andExpect(model().attributeExists("restaurantName"))
                .andExpect(model().attribute("restaurantName", restaurantDTO.getRestaurantName()))
                .andExpect(model().attributeExists("dishListDTO"))
                .andExpect(model().attribute("dishListDTO", dishesDTO))
                .andExpect(model().attributeExists("orderId"))
                .andExpect(model().attribute("orderId", 1))
                .andExpect(model().attributeExists("orderValue"))
                .andExpect(model().attribute("orderValue", orderValue.toString()))
                .andExpect(view().name("order_processing"));
    }

    @Test
    void checkThatYouCanPayForOrderAsClient() throws Exception {
        //given
        Integer orderId = 1;

        //when
        when(supperOrderService.updateOrderToPaid(orderId)).thenReturn(true);

        ResultActions result = mockMvc.perform(post(ORDER_PAYMENT)
                .param("orderId", orderId.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/orders"));
    }

    @Test
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    void checkThatYouCanNotPayForOrderAsRestaurant() throws Exception {
        //given
        int orderId = 1;

        //when
        ResultActions result = mockMvc.perform(post(ORDER_PAYMENT)
                .param("orderId", Integer.toString(orderId))
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "Error:\n[You do not have the required authority to pay for this order.]"))
                .andExpect(view().name("error"));
    }

    @Test
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    void checkThatYouCanProceedOrderAsRestaurant() throws Exception {
        //given
        Integer orderId = 1;

        //when
        doNothing().when(supperOrderService).proceedOrder(orderId);

        ResultActions result = mockMvc.perform(post(PROCEED_ORDER)
                .param("orderId", orderId.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/orders"));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatYouCanNotProceedOrderAsClient() throws Exception {
        //given
        int orderId = 1;

        //when
        ResultActions result = mockMvc.perform(post(PROCEED_ORDER)
                .param("orderId", Integer.toString(orderId))
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "Error:\n[You do not have the required authority to proceed this order.]"))
                .andExpect(view().name("error"));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "FAIL")
    void checkThatExceptionIsThrownWhenTryingToCancelOrderAsOtherUserThanRestaurantOrClient() throws Exception {
        //given
        int orderId = 1;
        int statusId = 1;
        String orderDate = OffsetDateTime.now().minusMinutes(19).toString();

        //when
        ResultActions result = mockMvc.perform(post(CANCEL_ORDER)
                .param("orderId", Integer.toString(orderId))
                .param("statusId", Integer.toString(statusId))
                .param("orderDate", orderDate)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "Error:\n[You do not have the required authority to cancel this order.]"))
                .andExpect(view().name("error"));
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanOrCanNotCancelOrderBasedOnOrderStatus(
            Boolean exceptionExpected,
            Integer orderStatus
    ) throws Exception {
        //given
        int orderId = 1;
        var orderDate = OffsetDateTime.now().minusMinutes(19);

        //when
        when(offsetDateTimeMapper.mapStringToOffsetDateTime(orderDate.toString())).thenReturn(orderDate);

        ResultActions result = mockMvc.perform(post(CANCEL_ORDER)
                .param("orderId", Integer.toString(orderId))
                .param("statusId", Integer.toString(orderStatus))
                .param("orderDate", orderDate.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        if (exceptionExpected) {
            result
                    .andExpect(status().isNotAcceptable())
                    .andExpect(model().attributeExists("errorMessage"))
                    .andExpect(model().attribute("errorMessage", "Error:\n[Order with this status can not be cancelled]"))
                    .andExpect(view().name("error"));
        } else {
            result
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(view().name("redirect:/orders"));
        }
    }

    public static Stream<Arguments> checkThatYouCanOrCanNotCancelOrderBasedOnOrderStatus() {
        return Stream.of(
                Arguments.of(false, OrderStatus.NEW.getStatusId()),
                Arguments.of(false, OrderStatus.PAID.getStatusId()),
                Arguments.of(true, OrderStatus.ACCEPTED.getStatusId()),
                Arguments.of(true, OrderStatus.DELIVERY.getStatusId()),
                Arguments.of(true, OrderStatus.REALIZED.getStatusId())
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanNotCancelOrderCreatedMoreThan20MinutesAgo(
            Boolean exceptionExpected,
            OffsetDateTime orderDate
    ) throws Exception {
        //given
        int orderId = 1;
        int orderStatus = 1;

        //when
        when(offsetDateTimeMapper.mapStringToOffsetDateTime(orderDate.toString())).thenReturn(orderDate);
        doNothing().when(supperOrderService).cancelOrder(orderId);

        ResultActions result = mockMvc.perform(post(CANCEL_ORDER)
                .param("orderId", Integer.toString(orderId))
                .param("statusId", Integer.toString(orderStatus))
                .param("orderDate", orderDate.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        if (exceptionExpected) {
            result
                    .andExpect(status().isNotAcceptable())
                    .andExpect(model().attributeExists("errorMessage"))
                    .andExpect(model().attribute("errorMessage", "Error:\n[More than 20 minutes passed. Order can not be cancelled]"))
                    .andExpect(view().name("error"));
        } else {
            result
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(view().name("redirect:/orders"));
        }
    }

    public static Stream<Arguments> checkThatYouCanNotCancelOrderCreatedMoreThan20MinutesAgo() {
        return Stream.of(
                Arguments.of(false, OffsetDateTime.now().minusMinutes(19)),
                Arguments.of(false, OffsetDateTime.now().minusMinutes(20)),
                Arguments.of(true, OffsetDateTime.now().minusMinutes(21))
        );
    }

//    public enum OrderStatus {
//        NEW(1),
//        PAID(2),
//        ACCEPTED(3),
//        DELIVERY(4),
//        REALIZED(5),
//        CANCELED(6);
}