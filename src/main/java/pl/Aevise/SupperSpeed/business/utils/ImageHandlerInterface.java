package pl.Aevise.SupperSpeed.business.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageHandlerInterface {
    int MAX_LOGO_WIDTH = 200;
    int MAX_LOGO_HEIGHT = 200;

    String DEFAULT_IMAGE_FORMAT = "jpg";
    String DEFAULT_IMAGE_STORAGE_FOLDER = "images/";

    BufferedImage resizeImage(BufferedImage image);

    BufferedImage changeTypeToJPG(byte[] imageBytes) throws IOException;

    String saveImage(BufferedImage resizedImage, String imageName, String folderPath) throws IOException;

}