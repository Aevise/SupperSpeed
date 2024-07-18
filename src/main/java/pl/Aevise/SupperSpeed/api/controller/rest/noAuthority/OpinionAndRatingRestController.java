package pl.Aevise.SupperSpeed.api.controller.rest.noAuthority;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Opinion And Rating Controller", description = "Endpoints used to fetch opinions and rating of restaurants")
public class OpinionAndRatingRestController {

    public static final String GET_OPINIONS = "/opinions/{restaurantName}";
    public static final String GET_TOTAL_RATING = "/rating";

    private final UserRatingService userRatingService;
    private final PageRequestUtils pageRequestUtils;

    @GetMapping(value = GET_OPINIONS)
    @Operation(summary = "Get opinions about given restaurant")
    public ResponseEntity<List<RestOpinionDTO>> getOpinionsAboutRestaurant(
            @Parameter(description = "Name of the restaurant", required = true)
            @PathVariable String restaurantName,
            @Parameter(description = "Sorting direction. Default ascending. Value must be <asc> or <desc>.", required = false)
            @RequestParam(value = "sort", required = false, defaultValue = "asc") String sortingDirection,
            @Parameter(description = "Current page of order's list.", required = false)
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Maximum number of opinions fetched in one request.", required = false)
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
    @Operation(summary = "Get total rating about restaurant")
    public ResponseEntity<TotalRestaurantRatingDTO> getTotalRestaurantRating(
            @Parameter(description = "Name of the restaurant", required = true)
            @RequestParam(value = "restaurantName") String restaurantName
    ) {
        if (Objects.isNull(restaurantName)) {
            return ResponseEntity.notFound().build();
        }
        TotalRestaurantRatingDTO restaurantRating = userRatingService.getRestaurantRating(restaurantName);

        return ResponseEntity.ok(restaurantRating);
    }
}
