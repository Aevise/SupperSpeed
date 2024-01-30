package pl.Aevise.SupperSpeed.business.utils;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageProcessing implements ImageProcessingInterface {
    @Override
    public BufferedImage resizeImage(BufferedImage image) {
        return Scalr.resize(
                image,
                Scalr.Method.ULTRA_QUALITY,
                Scalr.Mode.AUTOMATIC,
                ImageProcessingInterface.MAX_TARGET_WIDTH,
                ImageProcessingInterface.MAX_TARGET_HEIGHT,
                Scalr.OP_ANTIALIAS
        );
    }

    @Override
    public byte[] convertBufferedImageToBytes(BufferedImage image, String format) throws IOException {
        //TODO finish this function
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, format, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public BufferedImage convertBytesToBufferedImage(byte[] imageBytes, String format) throws IOException {
        //TODO finish this function
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(byteArrayInputStream);
    }
}
