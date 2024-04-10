package pl.Aevise.SupperSpeed.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.dto.DishListDTO;
import pl.Aevise.SupperSpeed.business.DishListService;
import pl.Aevise.SupperSpeed.business.SupperOrderService;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class OrderProcessingController {

    static final String ORDER_PROCESSING = "/orders/submit-order";
    static final String ORDER_PAYMENT = "/orders/submit-order/pay";

    private final SupperOrderService supperOrderService;
    private final DishListService dishListService;

    @PostMapping(ORDER_PROCESSING)
    public String getRestaurantMenu
            (
                    HttpServletRequest request,
                    @RequestParam(value = "restaurantId") Integer restaurantId,
                    @AuthenticationPrincipal UserDetails userDetails,
                    Model model
            ) {
        SupperOrderEntity newOrder = supperOrderService.createNewOrder(restaurantId, userDetails.getUsername());

        Map<Integer, Integer> dishesIdAndQuantities = extractDishIdAndAmount(request.getParameterMap());

        List<DishListDTO> dishListDTOS = dishListService.saveAllByOrderAndDishQuantity(newOrder.getOrderId(), dishesIdAndQuantities);

        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("dishesIdAndQuantities", dishesIdAndQuantities);
        model.addAttribute("orderId", newOrder.getOrderId());
        return "order_processing";
    }

    @PostMapping(ORDER_PAYMENT)
    public String payForOrder(
            @RequestParam(value = "orderId") Integer orderId
    ) {

        supperOrderService.updateOrderToPaid(orderId);
        return "order_processing";
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
