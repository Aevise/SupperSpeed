package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.integration.configuration.PersistenceContainerTestConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.util.Constants.CUISINES;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DeliveryAddressListJpaRepositoryTest {

    private final RestaurantJpaRepository restaurantJpaRepository;

    private final DeliveryAddressListJpaRepository deliveryAddressListJpaRepository;

    private static PageRequest buildPageRequestForDeliveryAddressList() {
        return PageRequest.of(0, 10, Sort.by("deliveryAddressEntity.streetName").ascending());
    }

    private final static String street1 = "Jaskrawa1";
    private final static String city1 = "WARSZAWA";
    private final static String street2 = "Jaskrawa2";
    private final static String city2 = "WARSZAWA";

    @Test
    void checkThatYouCanGetAllDeliveryAddressesForRestaurantWithGivenId() {
        //given
        var savedRestaurants = restaurantJpaRepository.findAll();
        RestaurantEntity firstSavedRestaurant = savedRestaurants.get(1);
        RestaurantEntity secondSavedRestaurant = savedRestaurants.get(2);

        //when
        List<DeliveryAddressListEntity> deliveryAddressList1 = deliveryAddressListJpaRepository.getAllByRestaurantEntity_Id(
                firstSavedRestaurant.getId(),
                buildPageRequestForDeliveryAddressList()).toList();
        List<DeliveryAddressListEntity> deliveryAddressList2 = deliveryAddressListJpaRepository.getAllByRestaurantEntity_Id(
                secondSavedRestaurant.getId(),
                buildPageRequestForDeliveryAddressList()).toList();

        //then
        assertThat(deliveryAddressList1).doesNotContainNull().hasSize(2);
        assertThat(deliveryAddressList2).doesNotContainNull().hasSize(10);
    }

    @Test
    void checkThatYouCanGetDeliveryAddressesForGivenRestaurantId() {
        //given
        var savedRestaurants = restaurantJpaRepository.findAll();
        RestaurantEntity firstSavedRestaurant = savedRestaurants.get(1);
        RestaurantEntity secondSavedRestaurant = savedRestaurants.get(2);

        //when
        var deliveryAddresses1 = deliveryAddressListJpaRepository.getDeliveryAddressesForRestaurant(firstSavedRestaurant.getId());
        var deliveryAddresses2 = deliveryAddressListJpaRepository.getDeliveryAddressesForRestaurant(secondSavedRestaurant.getId());

        //then
        assertThat(deliveryAddresses1).doesNotContainNull().hasSize(2);
        assertThat(deliveryAddresses2).doesNotContainNull().hasSize(11);
    }

    @Test
    void checkIfYouCanGetAllRestaurantsByDeliveryAddressCityAndStreetName() {
        //given, when
        var restaurants1 = deliveryAddressListJpaRepository
                .getAllRestaurantsByCityAndStreetName(city1, street1, buildPageRequestForDeliveryAddressList()).toList();
        var restaurants2 = deliveryAddressListJpaRepository
                .getAllRestaurantsByCityAndStreetName(city2, street2, buildPageRequestForDeliveryAddressList()).toList();

        //then
        assertThat(restaurants1).doesNotContainNull().hasSize(1);
        assertThat(restaurants2).doesNotContainNull().hasSize(2);
    }

    @Test
    void checkIfYouCanGetAllRestaurantsByDeliveryAddressCityAndStreetNameAndCuisine() {
        //given
        String cuisine1 = "Spanish";
        String cuisine2 = "Italian";
        String cuisine3 = "none";

        //when
        var restaurants1 = deliveryAddressListJpaRepository.getAllRestaurantsByCityAndStreetNameAndCuisine(
                city1, street1, cuisine1, buildPageRequestForDeliveryAddressList()
        ).toList();
        var restaurants2 = deliveryAddressListJpaRepository.getAllRestaurantsByCityAndStreetNameAndCuisine(
                city2, street2, cuisine1, buildPageRequestForDeliveryAddressList()
        ).toList();
        var restaurants3 = deliveryAddressListJpaRepository.getAllRestaurantsByCityAndStreetNameAndCuisine(
                city2, street2, cuisine2, buildPageRequestForDeliveryAddressList()
        ).toList();
        var restaurants4 = deliveryAddressListJpaRepository.getAllRestaurantsByCityAndStreetNameAndCuisine(
                city2, street2, cuisine3, buildPageRequestForDeliveryAddressList()
        ).toList();

        //then
        assertThat(restaurants1).doesNotContainNull().hasSize(0);
        assertThat(restaurants2).doesNotContainNull().hasSize(1);
        assertThat(restaurants3).doesNotContainNull().hasSize(1);
        assertThat(restaurants4).hasSize(0);
    }

    @Test
    void findCuisinesFromRestaurantsDeliveringTo() {
        //given, when
        List<String> cuisines1 = deliveryAddressListJpaRepository.findCuisinesFromRestaurantsDeliveringTo(city1, street1);
        List<String> cuisines2 = deliveryAddressListJpaRepository.findCuisinesFromRestaurantsDeliveringTo(city2, street2);

        //then
        assertThat(cuisines1).doesNotContainNull().hasSize(1).contains(CUISINES.get("Italian"));
        assertThat(cuisines2).doesNotContainNull().hasSize(2).contains(CUISINES.get("Spanish"), CUISINES.get("Italian"));
    }
}