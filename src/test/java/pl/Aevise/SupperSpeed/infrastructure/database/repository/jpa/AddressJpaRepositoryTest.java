package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.integration.configuration.PersistenceContainerTestConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.util.Constants.*;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class AddressJpaRepositoryTest {

    private AddressJpaRepository addressJpaRepository;

    @Test
    void CheckThatYouCanGetAllDistinctCities() {
        //given
        var addresses = List.of(
                addressEntity1(),
                addressEntity2(),
                addressEntity3(),
                addressEntity4()
        );
        addresses.get(3).setCity(null);
        addressJpaRepository.saveAllAndFlush(addresses);

        //when
        List<String> distinctCities = addressJpaRepository.findDistinctCities();

        //then
        assertThat(distinctCities).doesNotContainNull().containsAnyOf(WARSZAWA, LUBLIN, CHELM)
                .hasSize(4);
    }
}