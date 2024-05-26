package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.dto.OpinionDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.RestaurantMapper;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.business.SupperOrderService;
import pl.Aevise.SupperSpeed.business.UserRatingService;
import pl.Aevise.SupperSpeed.domain.Restaurant;

import java.util.List;

@Controller
@AllArgsConstructor
public class OpinionController {
    static final String OPINION = "/opinion";

    private final UserRatingService userRatingService;

    private final SupperOrderService supperOrderService;

    private final RestaurantService restaurantService;

    @GetMapping(OPINION)
    public String showOpinionsAboutRestaurant(
            Model model,
            @RequestParam(value = "restaurantId") Integer restaurantId
    ) {
        RestaurantDTO restaurant = restaurantService.findRestaurantDTOById(restaurantId);
        List<OpinionDTO> opinionsAboutOrdersFromRestaurant = userRatingService.getOpinionsAboutOrdersFromRestaurant(restaurantId);

        var totalRating = userRatingService.getRestaurantRating(restaurantId);


        model.addAttribute("opinions", opinionsAboutOrdersFromRestaurant);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("totalRating", totalRating);

        return "opinion";
    }
}
