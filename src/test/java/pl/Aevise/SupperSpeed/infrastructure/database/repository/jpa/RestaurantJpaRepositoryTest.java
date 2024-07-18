package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.integration.configuration.PersistenceContainerTestConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.util.Constants.*;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class RestaurantJpaRepositoryTest {

    private RestaurantJpaRepository restaurantJpaRepository;

    @Test
    void checkThatYouCanFetchDistinctCitiesWithRestaurant() {
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
        assertThat(distinctCities).doesNotContainNull().hasSize(3);
        assertThat(distinctCities).containsExactlyInAnyOrder(WARSZAWA, LUBLIN, CHELM);
    }

    @Test
    void checkThatYouCanFetchAddressByRestaurantId() {
        //given
        var restaurants = List.of(
                restaurantEntity1(),
                restaurantEntity2()
        );
        restaurantJpaRepository.saveAllAndFlush(restaurants);
        var savedRestaurants = restaurantJpaRepository.findAll();
        RestaurantEntity savedRestaurant1 = savedRestaurants.get(0);
        RestaurantEntity savedRestaurant2 = savedRestaurants.get(1);


        //when
        Optional<AddressEntity> fetchedAddress1 = restaurantJpaRepository.findAddressByRestaurantId(savedRestaurant1.getId());
        Optional<AddressEntity> fetchedAddress2 = restaurantJpaRepository.findAddressByRestaurantId(savedRestaurant2.getId());
        AddressEntity testAddressEntity1 = null, testAddressEntity2 = null;
        if (fetchedAddress1.isPresent()) testAddressEntity1 = fetchedAddress1.get();
        if (fetchedAddress2.isPresent()) testAddressEntity2 = fetchedAddress2.get();

        //then
        assertThat(testAddressEntity1).isNotNull().isInstanceOf(AddressEntity.class).isEqualTo(savedRestaurant1.getAddress());
        assertThat(testAddressEntity2).isNotNull().isInstanceOf(AddressEntity.class).isEqualTo(savedRestaurant2.getAddress());
    }

    @Test
    void checkThatYouCanFindAllRestaurantsInCity() {
        //given
        var restaurants = List.of(
                restaurantEntity1(),
                restaurantEntity2(),
                restaurantEntity3()
        );
        restaurantJpaRepository.saveAllAndFlush(restaurants);

        //when
        List<RestaurantEntity> restaurantsInWarszawa = restaurantJpaRepository.findAllByAddress_City(WARSZAWA);
        List<RestaurantEntity> restaurantsInLublin = restaurantJpaRepository.findAllByAddress_City(LUBLIN);
        List<RestaurantEntity> restaurantsInPoznan = restaurantJpaRepository.findAllByAddress_City("Poznan");

        //then
        assertThat(restaurantsInWarszawa).doesNotContainNull().hasSize(2)
                .containsExactlyInAnyOrder(restaurantEntity1(), restaurantEntity2()).doesNotContain(restaurantEntity3());

        assertThat(restaurantsInLublin).doesNotContainNull().hasSize(3).contains(restaurantEntity3())
                .doesNotContain(restaurantEntity1(), restaurantEntity2());

        assertThat(restaurantsInPoznan).hasSize(0);
    }

    @Test
    void checkThatYouCanFindRestaurantByEmail() {
        //given
        var restaurants = List.of(
                restaurantEntity1(),
                restaurantEntity2()
        );
        restaurantJpaRepository.saveAllAndFlush(restaurants);

        //when
        Optional<RestaurantEntity> fetchedRestaurant1 = restaurantJpaRepository
                .findBySupperUser_Email(restaurantEntity1().getSupperUser().getEmail());
        Optional<RestaurantEntity> fetchedRestaurant2 = restaurantJpaRepository
                .findBySupperUser_Email(restaurantEntity2().getSupperUser().getEmail());
        RestaurantEntity testRestaurantEntity1 = null, testRestaurantEntity2 = null;

        if (fetchedRestaurant1.isPresent()) testRestaurantEntity1 = fetchedRestaurant1.get();
        if (fetchedRestaurant2.isPresent()) testRestaurantEntity2 = fetchedRestaurant2.get();

        //then
        assertThat(testRestaurantEntity1).isNotNull().isEqualTo(fetchedRestaurant1.get());
        assertThat(testRestaurantEntity2).isNotNull().isEqualTo(restaurantEntity2());
    }

    @Test
    void checkThatYouCanGetAllRestaurantsInCityWithCuisine() {
        //given
        String city = "Lublin";
        String cuisine = "Italian";

        //when
        List<RestaurantEntity> restaurants = restaurantJpaRepository.findAllByAddress_CityAndCuisine_Cuisine(city, cuisine);

        //then
        assertThat(restaurants).isNotEmpty();
        assertThat(restaurants.size()).isEqualTo(1);
    }
}