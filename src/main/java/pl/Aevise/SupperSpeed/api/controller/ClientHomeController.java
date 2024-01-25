package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.Aevise.SupperSpeed.api.dto.mapper.RestaurantMapper;
import pl.Aevise.SupperSpeed.business.RestaurantViewService;

@Controller
@AllArgsConstructor
public class ClientHomeController {

    static final String CLIENT_HOME = "/client";

    private final RestaurantMapper restaurantMapper;
    private final RestaurantViewService restaurantViewService;

    @RequestMapping(value = CLIENT_HOME)
    public String clientHomePage(Model model) {
        var availableRestaurants = restaurantViewService.availableRestaurants()
                .stream()
                .map(restaurantMapper::map)
                .toList();

        model.addAttribute("availableRestaurantsDTOs", availableRestaurants);

        return "client_main";
    }


}
