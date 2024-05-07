package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.Aevise.SupperSpeed.business.dao.ImageDAO;
import pl.Aevise.SupperSpeed.business.utils.ImageHandler;
import pl.Aevise.SupperSpeed.domain.Image;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class ImageHandlingService {

    private static final String LOGO = "-LOGO";

    private final ImageHandler imageHandler;

    private final ImageDAO imageDAO;
    private final RestaurantService restaurantService;
    private final DishService dishService;

    public void uploadLogo(byte[] imageBytes, Integer userId, String restaurantName) throws IOException {
        String directoryForRestaurant = getDirectoryForRestaurant(userId, restaurantName);
        String saveLocation;

        BufferedImage originalImageToJPG = imageHandler.changeTypeToJPG(imageBytes);
        BufferedImage resizedImage = imageHandler.resizeImageForLogo(originalImageToJPG);
        saveLocation = imageHandler.saveImage(resizedImage, restaurantName + LOGO, directoryForRestaurant);
        log.info("Saved resized logo for restaurant: [{}]", restaurantName + LOGO);

        Image image = imageDAO.saveImage(saveLocation);
        log.info("Saved logo in database: [{}]", image.getImageId());

        restaurantService.setLogo(image, userId);
    }

    public void uploadDishImage(byte[] imageBytes, Integer dishId, String dishName, String restaurantName, Integer restaurantId) {
        String directoryForRestaurant = getDirectoryForRestaurant(restaurantId, restaurantName);
        String saveLocation;

        BufferedImage originalImageToJPG = imageHandler.changeTypeToJPG(imageBytes);

        BufferedImage resizedImage = imageHandler.resizeImageForDish(originalImageToJPG);
        saveLocation = imageHandler.saveImage(resizedImage, dishName + "_" + dishId, directoryForRestaurant);
        log.info("Saved resized image for dish: [{}]", dishName);

        Image image = imageDAO.saveImage(saveLocation);
        log.info("Saved image in database: [{}]", image.getImageId());

        dishService.setDishImage(image, dishId);
    }

    public String getDirectoryForRestaurant(Integer userId, String restaurantName) {
        try {
            return imageHandler.createDirectoryForRestaurant(getRestaurantName(userId, restaurantName));
        } catch (IOException e) {
            log.error("Could not get directory for restaurant: [{}], id: [{}] - [{}]", restaurantName, userId, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getRestaurantName(Integer userId, String restaurantName) {
        return restaurantName + "_" + userId;
    }
}
