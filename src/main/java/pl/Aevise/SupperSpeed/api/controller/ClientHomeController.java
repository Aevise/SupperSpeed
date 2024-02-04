package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.Aevise.SupperSpeed.api.dto.mapper.RestaurantMapper;
import pl.Aevise.SupperSpeed.business.RestaurantViewService;

@Controller
@AllArgsConstructor
public class ClientHomeController {

    static final String CLIENT_HOME = "/client";

    private final RestaurantMapper restaurantMapper;
    private final RestaurantViewService restaurantViewService;

    @GetMapping(value = CLIENT_HOME)
    public String clientHomePage(Model model) {
        var availableRestaurants = restaurantViewService.availableRestaurants()
                .stream()
                .map(restaurantMapper::mapToDTO)
                .toList();

        model.addAttribute("availableRestaurantsDTOs", availableRestaurants);

        return "client";
    }
}
