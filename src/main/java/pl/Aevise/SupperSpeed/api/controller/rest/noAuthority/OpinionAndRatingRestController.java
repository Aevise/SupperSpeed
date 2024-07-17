package pl.Aevise.SupperSpeed.api.controller.rest.noAuthority;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.Aevise.SupperSpeed.api.controller.utils.URLConstants;
import pl.Aevise.SupperSpeed.api.controller.utils.interfaces.PageRequestUtils;
import pl.Aevise.SupperSpeed.api.dto.RestOpinionDTO;
import pl.Aevise.SupperSpeed.api.dto.TotalRestaurantRatingDTO;
import pl.Aevise.SupperSpeed.business.UserRatingService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(URLConstants.API_UNAUTH)
@AllArgsConstructor
public class OpinionAndRatingRestController {

    public static final String GET_OPINIONS = "/opinions/{restaurantName}";
    public static final String GET_TOTAL_RATING = "/rating";

    private final UserRatingService userRatingService;
    private final PageRequestUtils pageRequestUtils;

    @GetMapping(value = GET_OPINIONS)
    public ResponseEntity<List<RestOpinionDTO>> getOpinionsAboutRestaurant(
            @PathVariable String restaurantName,
            @RequestParam(value = "sort", required = false, defaultValue = "asc") String sortingDirection,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "dishesOnPage", required = false, defaultValue = "10") Integer dishesOnPage
    ) {
        if (Objects.isNull(restaurantName)) {
            return ResponseEntity.notFound().build();
        }
        List<RestOpinionDTO> ratedOpinions = userRatingService.getOpinionsAboutOrdersFromRestaurantForRest(
                        restaurantName, pageRequestUtils.buildPageRequestForRatedOrders(sortingDirection, page, dishesOnPage))
                .getContent();

        return ResponseEntity.ok(ratedOpinions);
    }

    @GetMapping(value = GET_TOTAL_RATING)
    public ResponseEntity<TotalRestaurantRatingDTO> getTotalRestaurantRating(
            @RequestParam(value = "restaurantName") String restaurantName
    ) {
        if (Objects.isNull(restaurantName)) {
            return ResponseEntity.notFound().build();
        }
        TotalRestaurantRatingDTO restaurantRating = userRatingService.getRestaurantRating(restaurantName);

        return ResponseEntity.ok(restaurantRating);
    }
}
