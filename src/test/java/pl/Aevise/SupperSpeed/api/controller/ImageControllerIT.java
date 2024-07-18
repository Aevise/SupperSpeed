package pl.Aevise.SupperSpeed.api.controller;

import jakarta.persistence.EntityNotFoundException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.ResourceUtils;
import pl.Aevise.SupperSpeed.api.dto.mapper.OffsetDateTimeMapper;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ImageEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.LogoJpaRepository;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;
import pl.Aevise.SupperSpeed.integration.configuration.FlywayManualMigrationsConfiguration;
import pl.Aevise.SupperSpeed.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.ImageController.UPLOAD_DISH_IMAGE;
import static pl.Aevise.SupperSpeed.api.controller.ImageController.UPLOAD_LOGO;
import static pl.Aevise.SupperSpeed.api.controller.RestaurantMenuEditionController.RESTAURANT_MENU_EDIT;
import static pl.Aevise.SupperSpeed.api.controller.RestaurantProfileController.RESTAURANT_PROFILE;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_FLYWAY_1;

@AutoConfigureMockMvc
@Import(FlywayManualMigrationsConfiguration.class)
@WithMockUser(username = TEST_RESTAURANT_EMAIL_FLYWAY_1, password = Constants.testPassword, authorities = "RESTAURANT")
class ImageControllerIT extends AbstractITConfiguration {

    private final int restaurantId = 3;
    private final String restaurantName = TEST_RESTAURANT_EMAIL_FLYWAY_1;
    private final String TEST_IMAGE_FOLDER = "images/" + restaurantName + "_" + restaurantId + "/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Flyway flyway;
    @Autowired
    private LogoJpaRepository logoJpaRepository;
    @Autowired
    private OffsetDateTimeMapper offsetDateTimeMapper;
    @Autowired
    private TestRestTemplate testRestTemplate;

    public static Stream<String> checkThatImageEndpointRequiresToBeLogged() {
        return Stream.of("/upload/logo", "/upload/dishImage");
    }

    @BeforeEach
    void recreateFlywayMigrations() {
        flyway.clean();
        flyway.migrate();
    }

    @AfterEach
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void deleteTestPhotosFromFolder() throws IOException {
        File directory = new File(TEST_IMAGE_FOLDER);
        Path pathToDirectory = Path.of(TEST_IMAGE_FOLDER);

        if (directory.exists()) {
            try (Stream<Path> files = Files.walk(pathToDirectory)) {
                files
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
        assertFalse(false, String.valueOf(Files.exists(pathToDirectory)));
    }

    @ParameterizedTest
    @MethodSource
    @WithMockUser
    void checkThatImageEndpointRequiresToBeLogged(String endpoint) {
        //given
        String url = String.format("http://localhost:%s%s%s", port, basePath, endpoint);
        String expectedPartOfHTML = "<form class=\"form-signin\" method=\"post\" action=\"/supperspeed/login\">";

        //when
        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        String body = response.getBody();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(body).contains(expectedPartOfHTML);
    }

    @Test
    void uploadLogo() throws Exception {
        //given
        String fileName = "C1.jpg";

        String date = offsetDateTimeMapper.mapOffsetDateTimeToStringForImages(OffsetDateTime.now());
        String expectedImageName = date + "-" + restaurantName + "-LOGO.jpg";
        File testImage1 = ResourceUtils.getFile("classpath:imagesForTesting/C1.jpg");
        FileInputStream inputStream = new FileInputStream(testImage1);
        MockMultipartFile file = new MockMultipartFile("image", fileName, "image/jpeg", inputStream);

        //when
        ResultActions result = mockMvc.perform(multipart(UPLOAD_LOGO)
                .file(file)
                .param("restaurantId", Integer.toString(restaurantId))
                .param("restaurantName", restaurantName));
        ImageEntity newImage = logoJpaRepository.findById(1).orElseThrow(
                () -> new EntityNotFoundException("Image not added"));
        File directoryShouldExist = new File(TEST_IMAGE_FOLDER);
        File fileShouldExist = new File(TEST_IMAGE_FOLDER + expectedImageName);

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_PROFILE));

        assertEquals(expectedImageName, newImage.getImageURL());
        assertTrue(directoryShouldExist.exists());
        assertTrue(directoryShouldExist.isDirectory());
        assertTrue(fileShouldExist.exists());
        assertTrue(fileShouldExist.isFile());
    }

    @Test
    void uploadDishImage() throws Exception {
        //given
        String fileName = "C1.jpg";
        int dishId = 1;
        String dishName = "Ryba po grecku";

        String date = offsetDateTimeMapper.mapOffsetDateTimeToStringForImages(OffsetDateTime.now());
        String expectedImageName = date + "-" + dishName + "_" + dishId + ".jpg";

        File testImage1 = ResourceUtils.getFile("classpath:imagesForTesting/C1.jpg");
        FileInputStream inputStream = new FileInputStream(testImage1);
        MockMultipartFile file = new MockMultipartFile("image", fileName, "image/jpeg", inputStream);

        //when
        ResultActions result = mockMvc.perform(multipart(UPLOAD_DISH_IMAGE)
                .file(file)
                .param("dishId", Integer.toString(dishId))
                .param("dishName", dishName)
                .param("restaurantId", Integer.toString(restaurantId))
                .param("restaurantName", restaurantName));
        ImageEntity newImage = logoJpaRepository.findById(1).orElseThrow(
                () -> new EntityNotFoundException("Image not added"));
        File directoryShouldExist = new File(TEST_IMAGE_FOLDER);
        File fileShouldExist = new File(TEST_IMAGE_FOLDER + expectedImageName);

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));

        assertEquals(expectedImageName, newImage.getImageURL());
        assertTrue(directoryShouldExist.exists());
        assertTrue(directoryShouldExist.isDirectory());
        assertTrue(fileShouldExist.exists());
        assertTrue(fileShouldExist.isFile());
    }
}