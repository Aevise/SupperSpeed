package pl.Aevise.SupperSpeed.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.controller.exception.IncorrectOrderStatus;
import pl.Aevise.SupperSpeed.api.dto.DishListDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.OffsetDateTimeMapper;
import pl.Aevise.SupperSpeed.business.DishListService;
import pl.Aevise.SupperSpeed.business.SupperOrderService;
import pl.Aevise.SupperSpeed.domain.exception.NotFoundException;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class OrderProcessingController {

    static final String ORDER_PROCESSING = "/orders/submit-order";
    static final String ORDER_PAYMENT = "/orders/pay";
    static final String CANCEL_ORDER = "/orders/cancel";
    static final String PROCEED_ORDER = "/orders/proceed";

    private final SupperOrderService supperOrderService;
    private final DishListService dishListService;

    private final OffsetDateTimeMapper offsetDateTimeMapper;

    private final SecurityService securityService;

    @PostMapping(ORDER_PROCESSING)
    public String getRestaurantMenu
            (
                    HttpServletRequest request,
                    @RequestParam(value = "restaurantId") Integer restaurantId,
                    @RequestParam(value = "restaurantName") String restaurantName,
                    @AuthenticationPrincipal UserDetails userDetails,
                    Model model
            ) {
        if (securityService.getUserAuthority().equalsIgnoreCase(AvailableRoles.RESTAURANT.toString())) {
            return "redirect:/orders";
        }

        Map<Integer, Integer> dishesIdAndQuantities = extractDishIdAndAmount(request.getParameterMap());
        if (!dishesIdAndQuantities.isEmpty()) {
            SupperOrderEntity newOrder = supperOrderService.createNewOrder(restaurantId, userDetails.getUsername());

            List<DishListDTO> dishListDTO = dishListService.saveAllByOrderAndDishQuantity(newOrder.getOrderId(), dishesIdAndQuantities);
            BigDecimal orderValue = supperOrderService.extractTotalOrderValue(dishListDTO);

            model.addAttribute("restaurantId", restaurantId);
            model.addAttribute("restaurantName", restaurantName);
            model.addAttribute("dishListDTO", dishListDTO);
            model.addAttribute("orderId", newOrder.getOrderId());
            model.addAttribute("orderValue", orderValue.toString());
            return "order_processing";
        }
        throw new NotFoundException("Restaurant menu");
    }

    @PostMapping(ORDER_PAYMENT)
    public String payForOrder(
            @RequestParam(value = "orderId") Integer orderId
    ) {
        var authority = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().stream().findFirst()
                .orElseThrow(() -> new AccessDeniedException("You do not have the required authority to pay for this order."))
                .getAuthority();

        if (authority.equals(AvailableRoles.CLIENT.name())) {
            supperOrderService.updateOrderToPaid(orderId);
            return "redirect:/orders";
        }
        throw new AccessDeniedException("You do not have the required authority to pay for this order.");
    }

    @PostMapping(PROCEED_ORDER)
    public String proceedOrder(
            @RequestParam(value = "orderId") Integer orderId
    ) {
        var authority = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().stream().findFirst()
                .orElseThrow(() -> new AccessDeniedException("You do not have the required authority to proceed this order."))
                .getAuthority();

        if (authority.equals(AvailableRoles.RESTAURANT.name())) {
            supperOrderService.proceedOrder(orderId);
            return "redirect:/orders";
        }
        throw new AccessDeniedException("You do not have the required authority to proceed this order.");

    }

    @PostMapping(CANCEL_ORDER)
    public String cancelOrder(
            @RequestParam(value = "orderId") Integer orderId,
            @RequestParam(value = "statusId") Integer statusId,
            @RequestParam(value = "orderDate") String orderDate
    ) {
        var authority = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().stream().findFirst()
                .orElseThrow(() -> new AccessDeniedException("You do not have the required authority to cancel this order."))
                .getAuthority();

        if (authority.equals(AvailableRoles.RESTAURANT.name()) || authority.equals(AvailableRoles.CLIENT.name())) {
            if (statusId > 2) {
                throw new IncorrectOrderStatus("Order with this status can not be cancelled");
            }

            if (!checkIfMoreThan20MinutesPassed(orderDate)) {
                supperOrderService.cancelOrder(orderId);
                return "redirect:/orders";
            }
            throw new IncorrectOrderStatus("More than 20 minutes passed. Order can not be cancelled");
        }
        throw new AccessDeniedException("You do not have the required authority to cancel this order.");
    }

    private boolean checkIfMoreThan20MinutesPassed(String orderDate) {
        OffsetDateTime dateOfOrder = offsetDateTimeMapper.mapStringToOffsetDateTime(orderDate);
        OffsetDateTime now = OffsetDateTime.now();
        Duration duration = Duration.between(dateOfOrder, now);
        return duration.toMinutes() > 20;
    }

    private Map<Integer, Integer> extractDishIdAndAmount(Map<String, String[]> requestData) {
        /*
         Map<K,V> K - dishId, V - amount
         */
        Map<Integer, Integer> dishQuantities = new HashMap<>();
        requestData.forEach(
                (dishKey, amount) -> {
                    if (dishKey.startsWith("amountOfDishWithId_") && amount.length > 0 && Integer.parseInt(amount[0]) > 0) {
                        int dishId = Integer.parseInt(dishKey.replace("amountOfDishWithId_", ""));
                        int quantity = Integer.parseInt(amount[0]);
                        dishQuantities.put(dishId, quantity);
                    }
                }
        );
        return dishQuantities;
    }
}
