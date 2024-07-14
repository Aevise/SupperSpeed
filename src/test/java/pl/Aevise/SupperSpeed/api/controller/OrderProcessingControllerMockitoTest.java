package pl.Aevise.SupperSpeed.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ExtendedModelMap;
import pl.Aevise.SupperSpeed.api.dto.DishListDTO;
import pl.Aevise.SupperSpeed.business.DishListService;
import pl.Aevise.SupperSpeed.business.SupperOrderService;
import pl.Aevise.SupperSpeed.domain.exception.NotFoundException;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static pl.Aevise.SupperSpeed.util.Constants.*;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.dishListDTO1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.restaurantDTO1;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.*;

@ExtendWith(MockitoExtension.class)
class OrderProcessingControllerMockitoTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private SupperOrderService supperOrderService;
    @Mock
    private DishListService dishListService;
    @Mock
    private SecurityService securityService;

    @InjectMocks
    private OrderProcessingController orderProcessingController;


    @Test
    void shouldRedirectToOrdersPageWhenUserWithRestaurantAuthorityTriesToPlaceOrder() {
        //given
        String authority = AvailableRoles.RESTAURANT.toString();
        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_1).password(testPassword).authorities(authority).build();
        Integer restaurantId = restaurantDTO1().getRestaurantId();
        String restaurantName = restaurantDTO1().getRestaurantName();

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(securityService.getUserAuthority()).thenReturn(authority);

        String result = orderProcessingController.getRestaurantMenu(request, restaurantId, restaurantName, userDetails, model);

        //then
        assertThat(result).isNotNull().isEqualTo("redirect:/orders");
    }

    @Test
    void shouldReturnErrorPageWhenReceivedParametersMapIsEmpty() {
        //given
        String authority = AvailableRoles.CLIENT.toString();
        UserDetails userDetails = User.withUsername(TEST_CLIENT_EMAIL_1).password(testPassword).authorities(authority).build();
        Integer restaurantId = restaurantDTO1().getRestaurantId();
        String restaurantName = restaurantDTO1().getRestaurantName();
        Map<String, String[]> emptyResult = Map.of();

        String expectedExceptionMessage = "Restaurant menu";

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(securityService.getUserAuthority()).thenReturn(authority);
        when(request.getParameterMap()).thenReturn(emptyResult);

        NotFoundException receivedException = assertThrows(NotFoundException.class,
                () -> orderProcessingController.getRestaurantMenu(request, restaurantId, restaurantName, userDetails, model));

        //then
        assertEquals(expectedExceptionMessage, receivedException.getMessage());
    }

    @Test
    void shouldCorrectlyProceedOrderWhenClientTriesToOrderSomeFood() {
        //given
        String authority = AvailableRoles.CLIENT.toString();
        UserDetails userDetails = User.withUsername(TEST_CLIENT_EMAIL_1).password(testPassword).authorities(authority).build();
        Integer restaurantId = restaurantDTO1().getRestaurantId();
        String restaurantName = restaurantDTO1().getRestaurantName();
        Map<String, String[]> servletResponse = Map.of("amountOfDishWithId_1", new String[]{"1"});
        SupperOrderEntity order = buildSupperOrderEntity(restaurantEntity1(), clientEntity1());
        order.setOrderId(1);
        Map<Integer, Integer> dishesAndQuantities = Map.of(1, 1);
        List<DishListDTO> dishes = List.of(dishListDTO1());

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(securityService.getUserAuthority()).thenReturn(authority);
        when(request.getParameterMap()).thenReturn(servletResponse);
        when(supperOrderService.createNewOrder(restaurantId, userDetails.getUsername())).thenReturn(order);
        when(dishListService.saveAllByOrderAndDishQuantity(order.getOrderId(), dishesAndQuantities)).thenReturn(dishes);
        when(supperOrderService.extractTotalOrderValue(dishes)).thenReturn(BigDecimal.ONE);

        String result = orderProcessingController.getRestaurantMenu(request, restaurantId, restaurantName, userDetails, model);

        //then
        assertThat(result).isNotNull().isEqualTo("order_processing");
        assertThat(model.get("restaurantId")).isNotNull().isEqualTo(restaurantId);
        assertThat(model.get("restaurantName")).isNotNull().isEqualTo(restaurantName);
        assertThat(model.get("dishListDTO")).isNotNull().isEqualTo(dishes);
        assertThat(model.get("orderId")).isNotNull().isEqualTo(order.getOrderId());
        assertThat(model.get("orderValue")).isNotNull().isEqualTo(BigDecimal.ONE.toString());
    }
}