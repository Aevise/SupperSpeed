package pl.Aevise.SupperSpeed.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.business.ClientService;
import pl.Aevise.SupperSpeed.business.DishListService;
import pl.Aevise.SupperSpeed.business.SupperOrderService;
import pl.Aevise.SupperSpeed.business.UserService;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;

import java.util.Arrays;
import java.util.HashMap;
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
        dishListService.saveAllByOrderAndDishQuantity(newOrder.getOrderId(), dishesIdAndQuantities);



        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("dishesIdAndQuantities", dishesIdAndQuantities);
        return "order_processing";
    }

    //TODO zmienić na postMapping
    @GetMapping(ORDER_PAYMENT)
    public String payForOrder(){

        return "order_processing";
    }

    private Map<Integer, Integer> extractDishIdAndAmount(Map<String, String[]> requestData){
        /*
         Map<K,V> K - dishId, V - amount
         */
        Map<Integer, Integer> dishQuantities = new HashMap<>();
        requestData.forEach(
                (dishKey, amount) -> {
                    if(dishKey.startsWith("amountOfDishWithId_") && amount.length > 0 && Integer.parseInt(amount[0]) > 0){
                        int dishId = Integer.parseInt(dishKey.replace("amountOfDishWithId_", ""));
                        int quantity = Integer.parseInt(amount[0]);
                        dishQuantities.put(dishId, quantity);
                    }
                }
        );
        return dishQuantities;
    }
}
