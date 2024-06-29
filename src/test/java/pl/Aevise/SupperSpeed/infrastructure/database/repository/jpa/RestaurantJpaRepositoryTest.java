package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.integration.configuration.PersistenceContainerTestConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class RestaurantJpaRepositoryTest {

    private RestaurantJpaRepository restaurantJpaRepository;

    @Test
    void findDistinctCitiesForRestaurants() {
        //given
        var restaurants = List.of(
                restaurantEntity1(),
                restaurantEntity2(),
                restaurantEntity3()
        );
        restaurantJpaRepository.saveAllAndFlush(restaurants);

        //when
        List<String> distinctCities = restaurantJpaRepository.findDistinctCitiesForRestaurants();

        //then
        assertThat(distinctCities).hasSize(2);

    }

//        @Query("SELECT r.address FROM RestaurantEntity r WHERE r.id = :restaurantId")
//    Optional<AddressEntity> findAddressByRestaurantId(Integer restaurantId);
    @Test
    void findAddressByRestaurantId() {
        //given
        var restaurants = List.of(
                restaurantEntity1(),
                restaurantEntity2()
        );
        restaurantJpaRepository.saveAllAndFlush(restaurants);

        //when
        Optional<AddressEntity> fetchedAddress1 = restaurantJpaRepository.findAddressByRestaurantId(restaurantEntity1().getId());
        Optional<AddressEntity> fetchedAddress2 = restaurantJpaRepository.findAddressByRestaurantId(restaurantEntity2().getId());
        AddressEntity testAddressEntity1 = null, testAddressEntity2 = null;

        if(fetchedAddress1.isPresent() && fetchedAddress2.isPresent()){
            testAddressEntity1 = fetchedAddress1.get();
            testAddressEntity2 = fetchedAddress2.get();
        }

        //then
        assertThat(testAddressEntity1).isNotNull().isEqualTo(addressEntity1());
        assertThat(testAddressEntity2).isNotNull().isEqualTo(addressEntity2());
    }
}