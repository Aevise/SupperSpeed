package pl.Aevise.SupperSpeed.api.controller.rest.noAuthority;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Image Controller (Internal)", description = "Endpoints used to fetch images from database. Used internally by other controllers")
public class ImageRestController {

    private final Path rootLocation = Paths.get(new File(DEFAULT_IMAGE_STORAGE_FOLDER).getAbsolutePath());

    @GetMapping("/images/{fileName:.+}")
    @Operation(summary = "Get dish image or restaurant logo from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved image"),
            @ApiResponse(responseCode = "400", description = "Error retrieving image"),
            @ApiResponse(responseCode = "404", description = "Image not found or not readable")
    })
    public ResponseEntity<Resource> getImage(
            @Parameter(description = "Name of image. Format:\nyyyy_MM_dd-<dishName>_<dishId>.jpg\nor\nyyyy_MM_dd-<{>restaurantName>_<restaurantId>-LOGO.jpg", required = true)
            @PathVariable String fileName
    ) {
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
