package pl.Aevise.SupperSpeed.business.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageProcessingInterface {
    int MAX_TARGET_WIDTH = 200;
    int MAX_TARGET_HEIGHT = 200;
    String DEFAULT_IMAGE_FORMAT = "jpg";

    BufferedImage resizeImage(BufferedImage image);

    byte[] convertBufferedImageToBytes(BufferedImage image, String format) throws IOException;

    BufferedImage convertBytesToBufferedImage(byte[] imageBytes, String format) throws IOException;

}
