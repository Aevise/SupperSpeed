package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.Aevise.SupperSpeed.api.dto.*;
import pl.Aevise.SupperSpeed.api.dto.mapper.StatusListMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.SupperOrderMapper;
import pl.Aevise.SupperSpeed.business.*;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.domain.StatusList;
import pl.Aevise.SupperSpeed.domain.SupperOrder;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.OrdersBrowseController.SUPPER_SPEED_ORDERS_BROWSER;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.*;

@WebMvcTest(controllers = OrdersBrowseController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class OrdersBrowseControllerWebMvcTest {

    private final MockMvc mockMvc;

    @MockBean
    private StatusListService statusListService;
    @MockBean
    private StatusListMapper statusListMapper;
    @MockBean
    private SecurityService securityService;
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private ClientProfileService clientProfileService;
    @MockBean
    private SupperOrderService supperOrderService;
    @MockBean
    private SupperOrderMapper supperOrderMapper;
    @MockBean
    private DishListService dishListService;

    @Test
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    void checkThatYouCanBrowseYourOrdersAsRestaurant() throws Exception {
        //given
        List<StatusList> statusList = List.of(statusList1());
        RestaurantDTO restaurant = restaurantDTO1();
        List<SupperOrder> orders = List.of(supperOrder1());
        List<SupperOrderDTO> orderDTOS = List.of(supperOrderDTO1());
        List<DishListDTO> dishes = List.of(dishListDTO1());
        Map<Integer, List<DishListDTO>> dishesByOrderID = Map.of(1, dishes);
        String authority = "RESTAURANT";

        //when
        when(statusListService.getStatusList()).thenReturn(statusList);
        when(statusListMapper.mapToDTO(statusList.get(0))).thenReturn(statusListDTO1());
        when(securityService.getUserAuthority()).thenReturn(authority);
        when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurant);
        when(supperOrderService.getOrdersByRestaurantId(restaurant.getRestaurantId())).thenReturn(orders);
        when(supperOrderMapper.mapToDTO(orders.get(0))).thenReturn(supperOrderDTO1());
        when(dishListService.getDishesByAllOrdersId(orderDTOS)).thenReturn(dishesByOrderID);

        ResultActions result = mockMvc.perform(get(SUPPER_SPEED_ORDERS_BROWSER)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("statusListDTO"))
                .andExpect(model().attribute("statusListDTO", List.of(statusListDTO1())))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("role"))
                .andExpect(model().attribute("role", authority))
                .andExpect(model().attributeExists("dishesByOrderId"))
                .andExpect(model().attribute("dishesByOrderId", dishesByOrderID))
                .andExpect(model().attributeExists("ordersTotalPrice"))
                .andExpect(model().attribute("ordersTotalPrice", Map.of(1, BigDecimal.ONE)))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attribute("userId", restaurant.getRestaurantId()))
                .andExpect(view().name("orders_page"));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatYouCanBrowseYourOrdersAsClient() throws Exception {
        //given
        List<StatusList> statusList = List.of(statusList1());
        Client client = client1();
        List<SupperOrder> orders = List.of(supperOrder1());
        List<SupperOrderDTO> orderDTOS = List.of(supperOrderDTO1());
        List<DishListDTO> dishes = List.of(dishListDTO1());
        Map<Integer, List<DishListDTO>> dishesByOrderID = Map.of(1, dishes);
        String authority = "CLIENT";

        //when
        when(statusListService.getStatusList()).thenReturn(statusList);
        when(statusListMapper.mapToDTO(statusList.get(0))).thenReturn(statusListDTO1());
        when(securityService.getUserAuthority()).thenReturn(authority);
        when(clientProfileService.findClientByEmail(TEST_CLIENT_EMAIL_1)).thenReturn(Optional.of(client));
        when(supperOrderService.getOrdersByClientId(client.getId())).thenReturn(orders);
        when(supperOrderMapper.mapToDTO(orders.get(0))).thenReturn(supperOrderDTO1());
        when(dishListService.getDishesByAllOrdersId(orderDTOS)).thenReturn(dishesByOrderID);

        ResultActions result = mockMvc.perform(get(SUPPER_SPEED_ORDERS_BROWSER)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("statusListDTO"))
                .andExpect(model().attribute("statusListDTO", List.of(statusListDTO1())))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("role"))
                .andExpect(model().attribute("role", authority))
                .andExpect(model().attributeExists("dishesByOrderId"))
                .andExpect(model().attribute("dishesByOrderId", dishesByOrderID))
                .andExpect(model().attributeExists("ordersTotalPrice"))
                .andExpect(model().attribute("ordersTotalPrice", Map.of(1, BigDecimal.ONE)))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attribute("userId", client.getId()))
                .andExpect(view().name("orders_page"));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatClientCanRateFinishedOrder() throws Exception {
        //given
        List<StatusList> statusList = List.of(statusList1());
        Client client = client1();
        List<SupperOrder> orders = List.of(supperOrder1());
        SupperOrderDTO supperOrderDTO = supperOrderDTO1();
        supperOrderDTO.setStatusListDTO(StatusListDTO.builder()
                .statusId(5)
                .build());
        List<SupperOrderDTO> orderDTOS = List.of(supperOrderDTO);
        List<DishListDTO> dishes = List.of(dishListDTO1());
        Map<Integer, List<DishListDTO>> dishesByOrderID = Map.of(1, dishes);
        String authority = "CLIENT";

        //when
        when(statusListService.getStatusList()).thenReturn(statusList);
        when(statusListMapper.mapToDTO(statusList.get(0))).thenReturn(statusListDTO1());
        when(securityService.getUserAuthority()).thenReturn(authority);
        when(clientProfileService.findClientByEmail(TEST_CLIENT_EMAIL_1)).thenReturn(Optional.of(client));
        when(supperOrderService.getOrdersByClientId(client.getId())).thenReturn(orders);
        when(supperOrderMapper.mapToDTO(orders.get(0))).thenReturn(supperOrderDTO);
        when(dishListService.getDishesByAllOrdersId(orderDTOS)).thenReturn(dishesByOrderID);

        ResultActions result = mockMvc.perform(get(SUPPER_SPEED_ORDERS_BROWSER)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("statusListDTO"))
                .andExpect(model().attribute("statusListDTO", List.of(statusListDTO1())))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("role"))
                .andExpect(model().attribute("role", authority))
                .andExpect(model().attributeExists("dishesByOrderId"))
                .andExpect(model().attribute("dishesByOrderId", dishesByOrderID))
                .andExpect(model().attributeExists("ordersTotalPrice"))
                .andExpect(model().attribute("ordersTotalPrice", Map.of(1, BigDecimal.ONE)))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attribute("userId", client.getId()))
                .andExpect(view().name("orders_page"));

        assertThat(content).contains("<div class=\"row\"><b>Rate the order!:</b></div>");
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatUserCommentIsDisplayed() throws Exception {
        //given
        List<StatusList> statusList = List.of(statusList1());
        Client client = client1();
        UserRatingDTO userRatingDTO = userRatingDTO1();
        List<SupperOrder> orders = List.of(supperOrder1());
        SupperOrderDTO supperOrderDTO = supperOrderDTO1();
        supperOrderDTO
                .setStatusListDTO(StatusListDTO.builder()
                        .statusId(5)
                        .build());
        supperOrderDTO.setUserRatingDTO(userRatingDTO);
        List<SupperOrderDTO> orderDTOS = List.of(supperOrderDTO);
        List<DishListDTO> dishes = List.of(dishListDTO1());
        Map<Integer, List<DishListDTO>> dishesByOrderID = Map.of(1, dishes);
        String authority = "CLIENT";

        //when
        when(statusListService.getStatusList()).thenReturn(statusList);
        when(statusListMapper.mapToDTO(statusList.get(0))).thenReturn(statusListDTO1());
        when(securityService.getUserAuthority()).thenReturn(authority);
        when(clientProfileService.findClientByEmail(TEST_CLIENT_EMAIL_1)).thenReturn(Optional.of(client));
        when(supperOrderService.getOrdersByClientId(client.getId())).thenReturn(orders);
        when(supperOrderMapper.mapToDTO(orders.get(0))).thenReturn(supperOrderDTO);
        when(dishListService.getDishesByAllOrdersId(orderDTOS)).thenReturn(dishesByOrderID);

        ResultActions result = mockMvc.perform(get(SUPPER_SPEED_ORDERS_BROWSER)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("statusListDTO"))
                .andExpect(model().attribute("statusListDTO", List.of(statusListDTO1())))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("role"))
                .andExpect(model().attribute("role", authority))
                .andExpect(model().attributeExists("dishesByOrderId"))
                .andExpect(model().attribute("dishesByOrderId", dishesByOrderID))
                .andExpect(model().attributeExists("ordersTotalPrice"))
                .andExpect(model().attribute("ordersTotalPrice", Map.of(1, BigDecimal.ONE)))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attribute("userId", client.getId()))
                .andExpect(view().name("orders_page"));

        assertThat(content).contains("<div class=\"col\"><b>User Rating:</b></div>");
        assertThat(content).contains("<div class=\"col\">Food Rating: " + userRatingDTO.getFoodRating() + "</div>");
        assertThat(content).contains("<div class=\"col\">Delivery Rating: " + userRatingDTO.getDeliveryRating() + "</div>");
        assertThat(content).contains("<div class=\"col\">Comment: " + userRatingDTO.getDescription() + "</div>");
    }

    @Test
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    void checkThatRestaurantCanResponseToUserComment() throws Exception {
        //given
        List<StatusList> statusList = List.of(statusList1());
        RestaurantDTO restaurant = restaurantDTO1();
        UserRatingDTO userRatingDTO = userRatingDTO1();
        List<SupperOrder> orders = List.of(supperOrder1());
        SupperOrderDTO supperOrderDTO = supperOrderDTO1();
        supperOrderDTO
                .setStatusListDTO(StatusListDTO.builder()
                        .statusId(5)
                        .build());
        supperOrderDTO.setUserRatingDTO(userRatingDTO);
        List<SupperOrderDTO> orderDTOS = List.of(supperOrderDTO);
        List<DishListDTO> dishes = List.of(dishListDTO1());
        Map<Integer, List<DishListDTO>> dishesByOrderID = Map.of(1, dishes);
        String authority = "RESTAURANT";

        //when
        when(statusListService.getStatusList()).thenReturn(statusList);
        when(statusListMapper.mapToDTO(statusList.get(0))).thenReturn(statusListDTO1());
        when(securityService.getUserAuthority()).thenReturn(authority);
        when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurant);
        when(supperOrderService.getOrdersByRestaurantId(restaurant.getRestaurantId())).thenReturn(orders);
        when(supperOrderMapper.mapToDTO(orders.get(0))).thenReturn(supperOrderDTO);
        when(dishListService.getDishesByAllOrdersId(orderDTOS)).thenReturn(dishesByOrderID);

        ResultActions result = mockMvc.perform(get(SUPPER_SPEED_ORDERS_BROWSER)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("statusListDTO"))
                .andExpect(model().attribute("statusListDTO", List.of(statusListDTO1())))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("role"))
                .andExpect(model().attribute("role", authority))
                .andExpect(model().attributeExists("dishesByOrderId"))
                .andExpect(model().attribute("dishesByOrderId", dishesByOrderID))
                .andExpect(model().attributeExists("ordersTotalPrice"))
                .andExpect(model().attribute("ordersTotalPrice", Map.of(1, BigDecimal.ONE)))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attribute("userId", restaurant.getRestaurantId()))
                .andExpect(view().name("orders_page"));

        assertThat(content).contains("<div class=\"col\"><b>User Rating:</b></div>");
        assertThat(content).contains("<div class=\"col\">Food Rating: " + userRatingDTO.getFoodRating() + "</div>");
        assertThat(content).contains("<div class=\"col\">Delivery Rating: " + userRatingDTO.getDeliveryRating() + "</div>");
        assertThat(content).contains("<div class=\"col\">Comment: " + userRatingDTO.getDescription() + "</div>");
        assertThat(content).contains("Write your response:");
    }

    @Test
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    void checkThatRestaurantCanResponseIsShown() throws Exception {
        //given
        List<StatusList> statusList = List.of(statusList1());
        RestaurantDTO restaurant = restaurantDTO1();
        UserRatingDTO userRatingDTO = userRatingDTO1();
        userRatingDTO.setRestaurantResponseDTO(restaurantResponseDTO1());
        List<SupperOrder> orders = List.of(supperOrder1());
        SupperOrderDTO supperOrderDTO = supperOrderDTO1();
        supperOrderDTO
                .setStatusListDTO(StatusListDTO.builder()
                        .statusId(5)
                        .build());
        supperOrderDTO.setUserRatingDTO(userRatingDTO);
        List<SupperOrderDTO> orderDTOS = List.of(supperOrderDTO);
        List<DishListDTO> dishes = List.of(dishListDTO1());
        Map<Integer, List<DishListDTO>> dishesByOrderID = Map.of(1, dishes);
        String authority = "RESTAURANT";

        //when
        when(statusListService.getStatusList()).thenReturn(statusList);
        when(statusListMapper.mapToDTO(statusList.get(0))).thenReturn(statusListDTO1());
        when(securityService.getUserAuthority()).thenReturn(authority);
        when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurant);
        when(supperOrderService.getOrdersByRestaurantId(restaurant.getRestaurantId())).thenReturn(orders);
        when(supperOrderMapper.mapToDTO(orders.get(0))).thenReturn(supperOrderDTO);
        when(dishListService.getDishesByAllOrdersId(orderDTOS)).thenReturn(dishesByOrderID);

        ResultActions result = mockMvc.perform(get(SUPPER_SPEED_ORDERS_BROWSER)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("statusListDTO"))
                .andExpect(model().attribute("statusListDTO", List.of(statusListDTO1())))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("role"))
                .andExpect(model().attribute("role", authority))
                .andExpect(model().attributeExists("dishesByOrderId"))
                .andExpect(model().attribute("dishesByOrderId", dishesByOrderID))
                .andExpect(model().attributeExists("ordersTotalPrice"))
                .andExpect(model().attribute("ordersTotalPrice", Map.of(1, BigDecimal.ONE)))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attribute("userId", restaurant.getRestaurantId()))
                .andExpect(view().name("orders_page"));

        assertThat(content).contains("<div class=\"col\"><b>User Rating:</b></div>");
        assertThat(content).contains("<div class=\"col\">Food Rating: " + userRatingDTO.getFoodRating() + "</div>");
        assertThat(content).contains("<div class=\"col\">Delivery Rating: " + userRatingDTO.getDeliveryRating() + "</div>");
        assertThat(content).contains("<div class=\"col\">Comment: " + userRatingDTO.getDescription() + "</div>");
        assertThat(content).contains("<div class=\"col\">" + restaurantResponseDTO1().getDescription() + "</div>");
    }
}