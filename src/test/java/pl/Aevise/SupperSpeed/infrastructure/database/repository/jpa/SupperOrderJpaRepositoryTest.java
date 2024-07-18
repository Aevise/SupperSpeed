package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.integration.configuration.PersistenceContainerTestConfiguration;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class SupperOrderJpaRepositoryTest {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final DishJpaRepository dishJpaRepository;
    private final DishesListJpaRepository dishesListJpaRepository;
    private final ClientJpaRepository clientJpaRepository;

    private final SupperOrderJpaRepository supperOrderJpaRepository;

    private static PageRequest buildPageRequestForRatedOrders() {
        return PageRequest.of(0, 10, Sort.by("orderId").ascending());
    }

    public static Stream<Arguments> checkThatYouCanGetAllRatedOrdersBasedOnRestaurantName() {
        return Stream.of(
                Arguments.of("restaurant3", 1),
                Arguments.of("restaurant2", 0)
        );
    }

    @BeforeEach
    void createDishesAndOrders() {
        //create restaurant
        RestaurantEntity restaurant = restaurantJpaRepository.saveAndFlush(restaurantEntity1());

        //create client
        ClientEntity client = clientJpaRepository.saveAndFlush(clientEntity1());

        //create dishes
        var dishes = List.of(
                dishEntity1(),
                dishEntity2()
        );
        for (DishEntity dish : dishes) {
            dish.setRestaurant(restaurant);
        }
        List<DishEntity> savedDishes = dishJpaRepository.saveAllAndFlush(dishes);

        //create orders
        SupperOrderEntity order1 = supperOrderJpaRepository.saveAndFlush(buildSupperOrderEntity(restaurant, client));
        SupperOrderEntity order2 = supperOrderJpaRepository.saveAndFlush(buildSupperOrderEntity(restaurant, client));

        order1.setUserRating(buildUserRating1());
        supperOrderJpaRepository.saveAndFlush(order1);


        //createDishesList
        var dishesList = List.of(
                buildDishesListEntity(savedDishes.get(0), order1),
                buildDishesListEntity(savedDishes.get(0), order2),
                buildDishesListEntity(savedDishes.get(1), order2)
        );
        dishesListJpaRepository.saveAllAndFlush(dishesList);
    }

    @Test
    void checkThatYouCanGetAllOrdersByRestaurantId() {
        //given
        var restaurant = restaurantJpaRepository.findAll().stream()
                .filter(r -> r.getRestaurantName().equals(restaurantEntity1().getRestaurantName()))
                .findFirst().orElseThrow();

        //when
        var orders = supperOrderJpaRepository.findAllByRestaurant_Id(restaurant.getId());

        //then
        assertThat(orders).doesNotContainNull().hasSize(2);
    }

    @Test
    void checkThatYouCanGetAllOrdersByClientId() {
        //given
        var client = clientJpaRepository.findAll().stream()
                .filter(c -> c.getName().equals(clientEntity1().getName()))
                .findFirst().orElseThrow();

        //when
        var orders = supperOrderJpaRepository.findAllByClient_Id(client.getId());

        //then
        assertThat(orders).doesNotContainNull().hasSize(2);
    }

    @Test
    void checkThatYouCanGetAllRatedOrdersBasedOnRestaurantIdPage() {
        //given
        var restaurant = restaurantJpaRepository.findAll().stream()
                .filter(r -> r.getRestaurantName().equals(restaurantEntity1().getRestaurantName()))
                .findFirst().orElseThrow();

        //when
        var orders = supperOrderJpaRepository
                .findAllByRestaurant_IdAndUserRatingIsNotNull(restaurant.getId(), buildPageRequestForRatedOrders())
                .toList();

        //then
        assertThat(orders).doesNotContainNull().hasSize(1);
    }

    @Test
    void checkThatYouCanGetAllRatedOrdersBasedOnRestaurantIdList() {
        //given
        var restaurant = restaurantJpaRepository.findAll().stream()
                .filter(r -> r.getRestaurantName().equals(restaurantEntity1().getRestaurantName()))
                .findFirst().orElseThrow();

        //when
        var orders = supperOrderJpaRepository
                .findAllByRestaurant_IdAndUserRatingIsNotNull(restaurant.getId(), buildPageRequestForRatedOrders());

        //then
        assertThat(orders).doesNotContainNull().hasSize(1);
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanGetAllRatedOrdersBasedOnRestaurantName(
            String restaurantName,
            Integer expectedAmount
    ) {
        //given, when
        var orders = supperOrderJpaRepository
                .findAllByRestaurant_RestaurantNameAndUserRatingIsNotNull(restaurantName, buildPageRequestForRatedOrders())
                .toList();

        //then
        assertThat(orders).doesNotContainNull().hasSize(expectedAmount);
    }
}