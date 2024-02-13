package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class OrdersBrowseController {
    static final String SUPPER_SPEED_ORDERS_BROWSER = "/orders";

    @GetMapping(SUPPER_SPEED_ORDERS_BROWSER)
    public String getOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
            ){
        return "orders_page";
    }
}
