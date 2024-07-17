package pl.Aevise.SupperSpeed.api.controller.rest.noAuthority;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.DEFAULT_IMAGE_STORAGE_FOLDER;

@RestController
@Slf4j
public class ImageRestController {

    private final Path rootLocation = Paths.get(new File(DEFAULT_IMAGE_STORAGE_FOLDER).getAbsolutePath());

    @GetMapping("/images/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            Path file = rootLocation.resolve(fileName).normalize();
            URI fileUri = file.toUri();
            log.info("Attempting to retrieve file: [{}]", fileUri);
            Resource resource = new UrlResource(fileUri);
            if (resource.exists() && resource.isReadable()) {
                log.info("Serving image: [{}]", fileUri);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                log.warn("Requested file not found or not readable: [{}]", fileUri);
                throw new RuntimeException("Could not read file or not found: " + fileName);
            }
        } catch (Exception e) {
            log.error("Error retrieving file: [{}]", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
