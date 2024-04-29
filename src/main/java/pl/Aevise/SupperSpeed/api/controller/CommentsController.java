package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.dto.RestaurantResponseDTO;
import pl.Aevise.SupperSpeed.api.dto.UserRatingDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.OffsetDateTimeMapper;
import pl.Aevise.SupperSpeed.business.RestaurantResponseService;
import pl.Aevise.SupperSpeed.business.UserRatingService;

import java.time.OffsetDateTime;

@Controller
@AllArgsConstructor
public class CommentsController {

    private static final String ADD_COMMENT = "/orders/comment/add";
    private static final String ADD_RESPONSE = "/orders/comment/respond";

    private final OffsetDateTimeMapper offsetDateTimeMapper;

    private final UserRatingService userRatingService;
    private final RestaurantResponseService restaurantResponseService;

    @PostMapping(ADD_COMMENT)
    public String addUserCommentToOrder(
            @ModelAttribute UserRatingDTO userRatingDTO,
            @RequestParam(name="orderId") Integer orderId
    ){

        String s = offsetDateTimeMapper.mapOffsetDateTimeToString(OffsetDateTime.now());
        OffsetDateTime offsetDateTime = offsetDateTimeMapper.mapStringToOffsetDateTime(s);


        userRatingService.saveNewComment(userRatingDTO, orderId);

        return "redirect:" + OrdersBrowseController.SUPPER_SPEED_ORDERS_BROWSER;
    }

    @PostMapping(ADD_RESPONSE)
    public String addRestaurantResponseToComment(
            @ModelAttribute RestaurantResponseDTO restaurantResponseDTO,
            @RequestParam(name="userRatingId") Integer userRatingId
    ){

        restaurantResponseService.saveRestaurantResponse(restaurantResponseDTO, userRatingId);


        return "redirect:" + OrdersBrowseController.SUPPER_SPEED_ORDERS_BROWSER;
    }
}
