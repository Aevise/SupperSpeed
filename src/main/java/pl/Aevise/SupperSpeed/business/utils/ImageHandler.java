package pl.Aevise.SupperSpeed.business.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import pl.Aevise.SupperSpeed.api.dto.mapper.OffsetDateTimeMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ImageHandler implements ImageHandlerInterface {

    private final OffsetDateTimeMapper offsetDateTimeMapper;

    @Override
    public BufferedImage resizeImage(BufferedImage image) {
        return Scalr.resize(
                image,
                Scalr.Method.ULTRA_QUALITY,
                Scalr.Mode.AUTOMATIC,
                ImageHandlerInterface.MAX_LOGO_WIDTH,
                ImageHandlerInterface.MAX_LOGO_HEIGHT,
                Scalr.OP_ANTIALIAS
        );
    }

    @Override
    public BufferedImage changeTypeToJPG(byte[] imageBytes){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
        BufferedImage inputImage;

        try {
            inputImage = ImageIO.read(byteArrayInputStream);
            log.info("Successfully read file");
        } catch (IOException e) {
            log.warn("Could not read file [{}]", e.getMessage());
            throw new RuntimeException(e);
        }

        BufferedImage changedType = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        changedType.createGraphics().drawImage(inputImage, 0, 0, null);
        log.info("Successfully changed file type to JPG");
        return changedType;
    }

    @Override
    public String saveImage(BufferedImage resizedImage, String imageName, String folderPath){
        String fileName = fileNameCreator(imageName);
        String saveLocation = folderPath + "\\" +  fileName;
        try {
            ImageIO.write(resizedImage, DEFAULT_IMAGE_FORMAT, new File(saveLocation));
            log.info("Successfully saved file: [{}]", fileName);
        } catch (IOException e) {
            log.warn("Could not save file: [{}] - [{}]", fileName, e.getMessage());
            throw new RuntimeException(e);
        }
        return fileName;
    }

    private String fileNameCreator(String imageName) {
        String date = offsetDateTimeMapper.mapOffsetDateTimeToStringForImages(OffsetDateTime.now());
        return date + "-" + imageName + "." + DEFAULT_IMAGE_FORMAT;
    }

    public String createDirectoryForRestaurant(String restaurantName) throws IOException {
        String absolutePath = new File(DEFAULT_IMAGE_STORAGE_FOLDER).getAbsolutePath() + "\\" + restaurantName;
        Files.createDirectories(Paths.get(absolutePath));
        return absolutePath;
    }
}
