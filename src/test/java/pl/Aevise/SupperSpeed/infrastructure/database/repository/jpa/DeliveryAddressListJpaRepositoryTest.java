package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;
import pl.Aevise.SupperSpeed.integration.configuration.PersistenceContainerTestConfiguration;
import pl.Aevise.SupperSpeed.util.EntityFixtures;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DeliveryAddressListJpaRepositoryTest {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final DeliveryAddressJpaRepository deliveryAddressJpaRepository;

    private final DeliveryAddressListJpaRepository deliveryAddressListJpaRepository;

    private static PageRequest buildPageRequestForDeliveryAddressList() {
        return PageRequest.of(0, 10, Sort.by("deliveryAddressEntity.streetName").ascending());
    }

    @BeforeEach
    void bindRestaurantsWithDeliveryAddresses(){
        var restaurants = List.of(
                restaurantEntity1(),
                restaurantEntity2()
        );
        var deliveryAddresses = List.of(
                deliveryAddressEntity1(),
                deliveryAddressEntity2(),
                deliveryAddressEntity3()
        );
        deliveryAddressJpaRepository.saveAllAndFlush(deliveryAddresses);
        restaurantJpaRepository.saveAllAndFlush(restaurants);

        var savedDeliveryAddresses = deliveryAddressJpaRepository.findAll();
        var savedRestaurants = restaurantJpaRepository.findAll();

        RestaurantEntity firstSavedRestaurant = savedRestaurants.get(0);
        RestaurantEntity secondSavedRestaurant = savedRestaurants.get(1);
        var deliveryAddressesList = List.of(
                DeliveryAddressListEntity.builder()
                        .deliveryAddressEntity(savedDeliveryAddresses.get(0))
                        .restaurantEntity(firstSavedRestaurant)
                        .id(DeliveryAddressKey.builder()
                                .deliveryAddressId(savedDeliveryAddresses.get(0).getDeliveryAddressId())
                                .restaurantId(firstSavedRestaurant.getId())
                                .build())
                        .build(),
                DeliveryAddressListEntity.builder()
                        .deliveryAddressEntity(savedDeliveryAddresses.get(1))
                        .restaurantEntity(firstSavedRestaurant)
                        .id(DeliveryAddressKey.builder()
                                .deliveryAddressId(savedDeliveryAddresses.get(1).getDeliveryAddressId())
                                .restaurantId(firstSavedRestaurant.getId())
                                .build())
                        .build(),
                DeliveryAddressListEntity.builder()
                        .deliveryAddressEntity(savedDeliveryAddresses.get(2))
                        .restaurantEntity(secondSavedRestaurant)
                        .id(DeliveryAddressKey.builder()
                                .deliveryAddressId(savedDeliveryAddresses.get(2).getDeliveryAddressId())
                                .restaurantId(secondSavedRestaurant.getId())
                                .build())
                        .build(),
                DeliveryAddressListEntity.builder()
                        .deliveryAddressEntity(savedDeliveryAddresses.get(1))
                        .restaurantEntity(savedRestaurants.get(2))
                        .id(DeliveryAddressKey.builder()
                                .deliveryAddressId(savedDeliveryAddresses.get(1).getDeliveryAddressId())
                                .restaurantId(savedRestaurants.get(2).getId())
                                .build())
                        .build()
        );
        deliveryAddressListJpaRepository.saveAllAndFlush(deliveryAddressesList);
    }

    @Test
    void checkThatYouCanGetAllDeliveryAddressesForRestaurantWithGivenId() {
        //given
        var savedRestaurants = restaurantJpaRepository.findAll();
        RestaurantEntity firstSavedRestaurant = savedRestaurants.get(0);
        RestaurantEntity secondSavedRestaurant = savedRestaurants.get(1);

        //when
        List<DeliveryAddressListEntity> deliveryAddressList1 = deliveryAddressListJpaRepository.getAllByRestaurantEntity_Id(
                firstSavedRestaurant.getId(),
                buildPageRequestForDeliveryAddressList()).toList();
        List<DeliveryAddressListEntity> deliveryAddressList2 = deliveryAddressListJpaRepository.getAllByRestaurantEntity_Id(
                secondSavedRestaurant.getId(),
                buildPageRequestForDeliveryAddressList()).toList();

        //then
        assertThat(deliveryAddressList1).doesNotContainNull().hasSize(2);
        assertThat(deliveryAddressList2).doesNotContainNull().hasSize(1);
    }

    @Test
    void checkThatYouCanGetDeliveryAddressesForGivenRestaurantId() {
        //given
        var savedRestaurants = restaurantJpaRepository.findAll();
        RestaurantEntity firstSavedRestaurant = savedRestaurants.get(0);
        RestaurantEntity secondSavedRestaurant = savedRestaurants.get(1);

        //when
        var deliveryAddresses1 = deliveryAddressListJpaRepository.getDeliveryAddressesForRestaurant(firstSavedRestaurant.getId());
        var deliveryAddresses2 = deliveryAddressListJpaRepository.getDeliveryAddressesForRestaurant(secondSavedRestaurant.getId());

        //then
        assertThat(deliveryAddresses1).doesNotContainNull().hasSize(2);
        assertThat(deliveryAddresses2).doesNotContainNull().hasSize(1);
    }

    @Test
    void checkIfYouCanGetAllRestaurantsByDeliveryAddressCityAndStreetName() {
        //given
        String street1 = deliveryAddressEntity1().getStreetName();
        String city1 = deliveryAddressEntity1().getCity();
        String street2 = deliveryAddressEntity2().getStreetName();
        String city2 = deliveryAddressEntity2().getCity();

        //when
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
        String street1 = deliveryAddressEntity1().getStreetName();
        String city1 = deliveryAddressEntity1().getCity();
        String street2 = deliveryAddressEntity2().getStreetName();
        String city2 = deliveryAddressEntity2().getCity();
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
        assertThat(restaurants1).doesNotContainNull().hasSize(1);
        assertThat(restaurants2).doesNotContainNull().hasSize(1);
        assertThat(restaurants3).doesNotContainNull().hasSize(1);
        assertThat(restaurants4).hasSize(0);
    }

//    @Query("SELECT DISTINCT r.restaurantEntity.cuisine.cuisine FROM DeliveryAddressListEntity r WHERE r.deliveryAddressEntity.city = :city AND r.deliveryAddressEntity.streetName = :streetName")
//    List<String> findCuisinesFromRestaurantsDeliveringTo(String city, String streetName);
    @Test
    void findCuisinesFromRestaurantsDeliveringTo() {
        //given
        String street1 = deliveryAddressEntity1().getStreetName();
        String city1 = deliveryAddressEntity1().getCity();
        String street2 = deliveryAddressEntity2().getStreetName();
        String city2 = deliveryAddressEntity2().getCity();

        //when
        List<String> cuisines1 = deliveryAddressListJpaRepository.findCuisinesFromRestaurantsDeliveringTo(city1, street1);
        List<String> cuisines2 = deliveryAddressListJpaRepository.findCuisinesFromRestaurantsDeliveringTo(city2, street2);

        //then
        assertThat(cuisines1).doesNotContainNull().hasSize(1).contains(CUISINES.get("Spanish"));
        assertThat(cuisines2).doesNotContainNull().hasSize(2).contains(CUISINES.get("Spanish"));
    }
}