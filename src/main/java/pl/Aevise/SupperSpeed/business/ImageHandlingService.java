package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.Aevise.SupperSpeed.business.dao.ImageDAO;
import pl.Aevise.SupperSpeed.business.utils.ImageHandler;
import pl.Aevise.SupperSpeed.domain.Logo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class ImageHandlingService {

    private static final String LOGO = "-LOGO";

    private final ImageHandler imageHandler;

    private final ImageDAO imageDAO;
    private final RestaurantService restaurantService;

    public void updateImage(byte[] imageBytes, Integer userId, String restaurantName, String dishName) throws IOException {
        String directoryForRestaurant = getDirectoryForRestaurant(userId, restaurantName);
        String saveLocation;

        BufferedImage originalImageToJPG = imageHandler.changeTypeToJPG(imageBytes);
        BufferedImage resizedImage = imageHandler.resizeImage(originalImageToJPG);
        if(dishName == null){
            saveLocation = imageHandler.saveImage(resizedImage, restaurantName + LOGO, directoryForRestaurant);
        }else {
            saveLocation = imageHandler.saveImage(resizedImage, dishName, directoryForRestaurant);
        }

        Logo logo = imageDAO.saveImage(saveLocation);

        restaurantService.setLogo(logo, userId);
    }

    public String getDirectoryForRestaurant(Integer userId, String restaurantName)  {
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
