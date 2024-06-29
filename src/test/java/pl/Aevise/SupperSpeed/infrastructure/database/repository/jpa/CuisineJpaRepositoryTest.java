package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.CuisineEntity;
import pl.Aevise.SupperSpeed.integration.configuration.PersistenceContainerTestConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class CuisineJpaRepositoryTest {

    private final CuisineJpaRepository cuisineJpaRepository;

    @Test
    void checkThatYouCanFetchCuisineByCuisineName() {
        //given
        List<CuisineEntity> cuisines = List.of(
                cuisineEntity1(),
                cuisineEntity2()
        );

        //when
        var fetchedCuisine1 = cuisineJpaRepository.findByCuisine(CUISINES.get("Italian"));
        var fetchedCuisine2 = cuisineJpaRepository.findByCuisine(CUISINES.get("Polish"));
        var fetchedCuisine3 = cuisineJpaRepository.findByCuisine("lolo lilo lo");
        CuisineEntity testCuisineEntity1 = null, testCuisineEntity2 = null, testCuisineEntity3 = null;

        if (fetchedCuisine1.isPresent()) testCuisineEntity1 = fetchedCuisine1.get();
        if (fetchedCuisine2.isPresent()) testCuisineEntity2 = fetchedCuisine2.get();
        if (fetchedCuisine3.isPresent()) testCuisineEntity3 = fetchedCuisine3.get();

        //then
        assertThat(testCuisineEntity1).isNotNull().isInstanceOf(CuisineEntity.class).isIn(cuisines);
        assertThat(testCuisineEntity2).isNotNull().isInstanceOf(CuisineEntity.class).isIn(cuisines);
        assertThat(testCuisineEntity3).isNull();
    }
}