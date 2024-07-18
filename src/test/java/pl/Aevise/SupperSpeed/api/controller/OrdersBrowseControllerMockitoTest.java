package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import pl.Aevise.SupperSpeed.api.dto.DishListDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.SupperOrderDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.StatusListMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.SupperOrderMapper;
import pl.Aevise.SupperSpeed.business.*;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.domain.SupperOrder;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.Aevise.SupperSpeed.util.Constants.*;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.client1;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.supperOrder1;

@ExtendWith(MockitoExtension.class)
class OrdersBrowseControllerMockitoTest {

    @Mock
    private StatusListService statusListService;
    @Mock
    private StatusListMapper statusListMapper;
    @Mock
    private SecurityService securityService;
    @Mock
    private RestaurantService restaurantService;
    @Mock
    private ClientProfileService clientProfileService;
    @Mock
    private SupperOrderService supperOrderService;
    @Mock
    private SupperOrderMapper supperOrderMapper;
    @Mock
    private DishListService dishListService;

    @InjectMocks
    private OrdersBrowseController ordersBrowseController;

    @Test
    void checkThatYouCanCorrectlyLoadOrdersAsRestaurantUser() {
        //given
        String authority = AvailableRoles.RESTAURANT.toString();
        RestaurantDTO restaurant = restaurantDTO1();
        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_1).password(testPassword).authorities(authority).build();
        List<SupperOrder> orders = List.of(supperOrder1());
        List<SupperOrderDTO> orderDTOS = List.of(supperOrderDTO1());
        List<DishListDTO> dishes = List.of(dishListDTO1());
        Map<Integer, List<DishListDTO>> dishesByOrderID = Map.of(1, dishes);

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(securityService.getUserAuthority()).thenReturn(authority);
        when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurant);
        when(supperOrderService.getOrdersByRestaurantId(restaurant.getRestaurantId())).thenReturn(orders);
        when(supperOrderMapper.mapToDTO(orders.get(0))).thenReturn(supperOrderDTO1());
        when(dishListService.getDishesByAllOrdersId(orderDTOS)).thenReturn(dishesByOrderID);

        ReflectionTestUtils.invokeMethod(ordersBrowseController, "getUserId", authority, TEST_RESTAURANT_EMAIL_1);

        String result = ordersBrowseController.getOrders(userDetails, model);

        //then
        assertThat(result).isNotNull().isEqualTo("orders_page");
        assertThat(model.get("statusListDTO")).isEqualTo(List.of());
        assertThat(model.get("role")).isNotNull().isEqualTo(authority);
        assertThat(model.get("dishesByOrderId")).isNotNull().isEqualTo(dishesByOrderID);
        assertThat(model.get("ordersTotalPrice")).isNotNull().isEqualTo(
                Map.of(1, BigDecimal.ONE)
        );
        assertThat(model.get("userId")).isNotNull().isEqualTo(restaurant.getRestaurantId());
    }

    @Test
    void checkThatYouCanCorrectlyLoadOrdersAsClientUser() {
        //given
        String authority = AvailableRoles.CLIENT.toString();
        Client client = client1();
        UserDetails userDetails = User.withUsername(TEST_CLIENT_EMAIL_1).password(testPassword).authorities(authority).build();
        List<SupperOrder> orders = List.of(supperOrder1());
        List<SupperOrderDTO> orderDTOS = List.of(supperOrderDTO1());
        List<DishListDTO> dishes = List.of(dishListDTO1());
        Map<Integer, List<DishListDTO>> dishesByOrderID = Map.of(1, dishes);

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(securityService.getUserAuthority()).thenReturn(authority);
        when(clientProfileService.findClientByEmail(TEST_CLIENT_EMAIL_1)).thenReturn(Optional.of(client));
        when(supperOrderService.getOrdersByClientId(client.getId())).thenReturn(orders);
        when(supperOrderMapper.mapToDTO(orders.get(0))).thenReturn(supperOrderDTO1());
        when(dishListService.getDishesByAllOrdersId(orderDTOS)).thenReturn(dishesByOrderID);

        ReflectionTestUtils.invokeMethod(ordersBrowseController, "getUserId", authority, TEST_CLIENT_EMAIL_1);

        String result = ordersBrowseController.getOrders(userDetails, model);

        //then
        assertThat(result).isNotNull().isEqualTo("orders_page");
        assertThat(model.get("statusListDTO")).isEqualTo(List.of());
        assertThat(model.get("role")).isNotNull().isEqualTo(authority);
        assertThat(model.get("dishesByOrderId")).isNotNull().isEqualTo(dishesByOrderID);
        assertThat(model.get("ordersTotalPrice")).isNotNull().isEqualTo(
                Map.of(1, BigDecimal.ONE)
        );
        assertThat(model.get("userId")).isNotNull().isEqualTo(client.getId());
    }
}