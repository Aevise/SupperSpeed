package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.Aevise.SupperSpeed.business.dao.ImageDAO;
import pl.Aevise.SupperSpeed.business.dao.RestaurantDAO;
import pl.Aevise.SupperSpeed.business.utils.ImageHandler;
import pl.Aevise.SupperSpeed.domain.Logo;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class ImageHandlingService {

    private final ImageHandler imageHandler;

    private final ImageDAO imageDAO;
    private final RestaurantService restaurantService;

    public void updateImage(byte[] imageBytes, Integer userId, String imageName) throws IOException {

        BufferedImage originalImageToJPG = imageHandler.changeTypeToJPG(imageBytes);
        BufferedImage resizedImage = imageHandler.resizeImage(originalImageToJPG);
        String saveLocation = imageHandler.saveImage(resizedImage, imageName);

        Logo logo = imageDAO.saveImage(saveLocation);
        //TODO update restaurant entity with new logo
    }
}
