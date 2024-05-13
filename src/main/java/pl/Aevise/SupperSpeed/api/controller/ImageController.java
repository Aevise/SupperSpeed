package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.Aevise.SupperSpeed.business.ImageHandlingService;

import java.io.IOException;

import static pl.Aevise.SupperSpeed.api.controller.RestaurantMenuEditionController.RESTAURANT_MENU_EDIT;
import static pl.Aevise.SupperSpeed.api.controller.RestaurantProfileController.RESTAURANT_PROFILE;

@Controller
@AllArgsConstructor
public class ImageController {

    static final String UPLOAD_LOGO = "/upload/logo";
    static final String UPLOAD_DISH_IMAGE = "/upload/dishImage";

    private final ImageHandlingService imageHandlingService;

    @PostMapping(UPLOAD_LOGO)
    public String uploadLogo(
            @RequestParam("image") MultipartFile image,
            @RequestParam("restaurantId") Integer restaurantId,
            @RequestParam("restaurantName") String restaurantName
    ) throws IOException {

        imageHandlingService.uploadLogo(image.getBytes(), restaurantId, restaurantName);

        return "redirect:" + RESTAURANT_PROFILE;
    }

    @PostMapping(UPLOAD_DISH_IMAGE)
    public String uploadDishImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("dishId") Integer dishId,
            @RequestParam("dishName") String dishName,
            @RequestParam("restaurantName") String restaurantName,
            @RequestParam("restaurantId") Integer restaurantId
    ) throws IOException {

        imageHandlingService.uploadDishImage(image.getBytes(), dishId, dishName, restaurantName, restaurantId);

        return "redirect:" + RESTAURANT_MENU_EDIT;
    }
}
