package pl.Aevise.SupperSpeed.api.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.controller.exception.UserNotFoundException;
import pl.Aevise.SupperSpeed.api.dto.RestaurantResponseDTO;
import pl.Aevise.SupperSpeed.api.dto.UserRatingDTO;
import pl.Aevise.SupperSpeed.business.RestaurantResponseService;
import pl.Aevise.SupperSpeed.business.UserRatingService;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import static pl.Aevise.SupperSpeed.api.controller.OrdersBrowseController.SUPPER_SPEED_ORDERS_BROWSER;

@Controller
@AllArgsConstructor
public class CommentsController {

    public static final String ADD_COMMENT = "/orders/comment/add";
    private static final String ADD_RESPONSE = "/orders/comment/respond";

    private final UserRatingService userRatingService;
    private final RestaurantResponseService restaurantResponseService;

    @PostMapping(ADD_COMMENT)
    public String addUserCommentToOrder(
            @ModelAttribute UserRatingDTO userRatingDTO,
            @RequestParam(name = "orderId") Integer orderId
    ) {
        var authority = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().stream().findFirst()
                .orElseThrow(()-> new UserNotFoundException("No user found"));
        if(authority.getAuthority().equals("ROLE_" + AvailableRoles.CLIENT.name())){
            userRatingService.saveNewComment(userRatingDTO, orderId);

            return "redirect:" + SUPPER_SPEED_ORDERS_BROWSER;
        }
        throw new AccessDeniedException("You do not have the required authority to add a comment to the order.");
    }

    @PostMapping(ADD_RESPONSE)
    public String addRestaurantResponseToComment(
            @ModelAttribute RestaurantResponseDTO restaurantResponseDTO,
            @RequestParam(name = "userRatingId") Integer userRatingId
    ) {
        var authority = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().stream().findFirst()
                .orElseThrow(()-> new UserNotFoundException("No user found"));
        if(authority.getAuthority().equals("ROLE_" + AvailableRoles.CLIENT.name())) {
            restaurantResponseService.saveRestaurantResponse(restaurantResponseDTO, userRatingId);
            return "redirect:" + SUPPER_SPEED_ORDERS_BROWSER;
        }
        throw new AccessDeniedException("You do not have the required authority to add a response to the comment in this order.");
    }
}
