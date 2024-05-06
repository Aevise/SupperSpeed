package pl.Aevise.SupperSpeed.business.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.Aevise.SupperSpeed.business.ImageHandlingService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.DEFAULT_IMAGE_FORMAT;

@Slf4j
@Service
@AllArgsConstructor
public class FileMigrationUtil {

    private final ImageHandlingService imageHandlingService;

    public void migrateFilesAfterRestaurantNameChange(Integer restaurantId, String oldName, String newName) {
        Path sourcePath = Paths.get(imageHandlingService.getDirectoryForRestaurant(restaurantId, oldName));
        Path targetPath = Paths.get(imageHandlingService.getDirectoryForRestaurant(restaurantId, newName));

        try (
                Stream<Path> files = Files.list(sourcePath)
        ) {
            files
                    .filter(path -> path.toString().endsWith(DEFAULT_IMAGE_FORMAT))
                    .forEach(source -> {
                        Path target = targetPath.resolve(source.getFileName());
                        try {
                            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                            Files.deleteIfExists(source);
                            log.info("Successfully copied and deleted file: [{}]", source.getFileName());
                        } catch (IOException e) {
                            log.warn("Failed to copy or delete file: [{}]", source.getFileName());
                        }
                    });
            Files.deleteIfExists(sourcePath);
            log.info("Deleted folder for old restaurant name: [{}]", oldName);
        } catch (IOException e) {
            log.error("Could not enter source path [{}] - [{}]", sourcePath, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }
}
