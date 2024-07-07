package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.controller.utils.PaginationAndSortingUtils;
import pl.Aevise.SupperSpeed.api.dto.OpinionDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.business.UserRatingService;

@Controller
@AllArgsConstructor
public class OpinionController {
    static final String OPINION = "/opinion";

    private final UserRatingService userRatingService;

    private final RestaurantService restaurantService;


    @GetMapping(OPINION)
    public String showOpinionsAboutRestaurant(
            Model model,
            @RequestParam(value = "restaurantId") Integer restaurantId,
            @RequestParam(value = "dir", required = false, defaultValue = "asc") String currDir,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer currPage
    ) {
        RestaurantDTO restaurant = restaurantService.findRestaurantDTOById(restaurantId);
        Page<OpinionDTO> opinionsAboutOrdersFromRestaurant = userRatingService.getOpinionsAboutOrdersFromRestaurant(restaurantId,
                buildPageRequestForRatedOrders(currDir, currPage));

        var totalRating = userRatingService.getRestaurantRating(restaurantId);


        model.addAttribute("opinions", opinionsAboutOrdersFromRestaurant.toList());
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("totalRating", totalRating);

        model.addAttribute("currDir", currDir);
        model.addAttribute("currPage", currPage);
        model.addAttribute("totalNumberOfPages", opinionsAboutOrdersFromRestaurant.getTotalPages());

        return "opinion";
    }

    private PageRequest buildPageRequestForRatedOrders(String direction, Integer page) {
        if (direction.equalsIgnoreCase(PaginationAndSortingUtils.ASC.getSortingDirection())) {
            return PageRequest.of(page,
                    10,
                    Sort.by("orderId").ascending());
        }
        return PageRequest.of(page,
                10,
                Sort.by("orderId").descending());
    }
}
