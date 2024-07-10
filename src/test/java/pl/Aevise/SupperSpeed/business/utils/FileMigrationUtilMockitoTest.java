package pl.Aevise.SupperSpeed.business.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.FileSystemUtils;
import pl.Aevise.SupperSpeed.business.ImageHandlingService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.restaurantDTO1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.restaurantDTO2;

@ExtendWith(MockitoExtension.class)
class FileMigrationUtilMockitoTest {

    @Mock
    private ImageHandlingService imageHandlingService;

    @InjectMocks
    private FileMigrationUtil fileMigrationUtil;

    private final static String TEST_FOLDER = "TestFolderDeleteIfExist";

    @AfterEach
    void deleteTestFolders() throws IOException {
        FileSystemUtils.deleteRecursively(Paths.get(TEST_FOLDER).toAbsolutePath());
    }

    @Test
    void checkThatOldRestaurantFolderIsDeletedCorrectly() throws IOException {
        //given
        int restaurantId = restaurantDTO1().getRestaurantId();
        String oldRestaurantName = restaurantDTO1().getRestaurantName();
        String newRestaurantName = restaurantDTO2().getRestaurantName();
        String oldAbsolutePath = new File(TEST_FOLDER).getAbsolutePath() + "\\" + oldRestaurantName;
        String newAbsolutePath = new File(TEST_FOLDER).getAbsolutePath() + "\\" + newRestaurantName;

        Path newPath = Paths.get(newAbsolutePath);
        Path oldPath = Paths.get(oldAbsolutePath);

        //when
        when(imageHandlingService.getDirectoryForRestaurant(restaurantId, oldRestaurantName)).thenReturn(oldAbsolutePath);
        when(imageHandlingService.getDirectoryForRestaurant(restaurantId, newRestaurantName)).thenReturn(newAbsolutePath);

        Files.createDirectories(oldPath);
        Files.createDirectories(newPath);

        fileMigrationUtil.migrateFilesAfterRestaurantNameChange(restaurantId, oldRestaurantName, newRestaurantName);

        //then
        assertTrue(Files.isDirectory(newPath));
        assertTrue(Files.notExists(oldPath));
    }

    @Test
    void checkThatFilesAreTransferredCorrectlyToTheNewFolder() throws IOException {
        //given
        int restaurantId = restaurantDTO1().getRestaurantId();
        String oldRestaurantName = restaurantDTO1().getRestaurantName();
        String newRestaurantName = restaurantDTO2().getRestaurantName();
        String oldAbsolutePath = new File(TEST_FOLDER).getAbsolutePath() + "\\" + oldRestaurantName;
        String newAbsolutePath = new File(TEST_FOLDER).getAbsolutePath() + "\\" + newRestaurantName;
        String testFolderWithImages = "C:\\Users\\aevir\\IdeaProjects\\SupperSpeed\\src\\test\\resources\\imagesForTesting";

        Path newPath = Paths.get(newAbsolutePath);
        Path oldPath = Paths.get(oldAbsolutePath);

        //when
        when(imageHandlingService.getDirectoryForRestaurant(restaurantId, oldRestaurantName)).thenReturn(oldAbsolutePath);
        when(imageHandlingService.getDirectoryForRestaurant(restaurantId, newRestaurantName)).thenReturn(newAbsolutePath);

        Files.createDirectories(oldPath);
        Files.createDirectories(newPath);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("file:" + testFolderWithImages + "/*");
            for (Resource resource : resources) {
                Path source = resource.getFile().toPath();
                Files.copy(source, newPath.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.err.println("Error test does not work");
        }

        fileMigrationUtil.migrateFilesAfterRestaurantNameChange(restaurantId, oldRestaurantName, newRestaurantName);

        //then
        assertTrue(Files.exists(newPath));
        assertTrue(Files.notExists(oldPath));
        assertThat(Files.list(newPath).count()).isEqualTo(3);
    }
}