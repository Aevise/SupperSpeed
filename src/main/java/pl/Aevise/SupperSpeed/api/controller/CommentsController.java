package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.dto.UserRatingDTO;
import pl.Aevise.SupperSpeed.business.UserRatingService;

@Controller
@AllArgsConstructor
public class CommentsController {

    private static final String ADD_COMMENT = "/orders/comment/add";

    private final UserRatingService userRatingService;

    @PostMapping(ADD_COMMENT)
    public String addUserCommentToOrder(
            @ModelAttribute UserRatingDTO userRatingDTO,
            @RequestParam(name="orderId") Integer orderId
    ){

        userRatingService.saveNewComment(userRatingDTO, orderId);

        return "redirect:" + OrdersBrowseController.SUPPER_SPEED_ORDERS_BROWSER;
    }
}
