package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.*;
import pl.Aevise.SupperSpeed.integration.configuration.PersistenceContainerTestConfiguration;
import pl.Aevise.SupperSpeed.util.EntityFixtures;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DishesListJpaRepositoryTest {

    private final DishesListJpaRepository dishesListJpaRepository;

    private final DishJpaRepository dishJpaRepository;
    private final SupperOrderJpaRepository supperOrderJpaRepository;
    private final RestaurantJpaRepository restaurantJpaRepository;

    @BeforeEach
    void createDishesAndOrders() {
        //create restaurant
        restaurantJpaRepository.save(restaurantEntity1());
        List<RestaurantEntity> all = restaurantJpaRepository.findAll();
        RestaurantEntity restaurant = all.stream()
                .filter(r -> r.getRestaurantName().equals(restaurantEntity1().getRestaurantName()))
                .findFirst()
                .orElseThrow();

        //create dishes
        var dishes = List.of(
                dishEntity1(),
                dishEntity2()
        );
        for (DishEntity dish : dishes) {
            dish.setRestaurant(restaurant);
        }
        dishJpaRepository.saveAllAndFlush(dishes);
        var fetchedDishes = dishJpaRepository.findAll().stream()
                .filter(d -> d.getName().equals(dishEntity1().getName())
                        || d.getName().equals(dishEntity2().getName()))
                .toList();

        //create orders
        supperOrderJpaRepository.saveAndFlush(buildSupperOrderEntity(restaurant));
        supperOrderJpaRepository.saveAndFlush(buildSupperOrderEntity(restaurant));

        var orders = supperOrderJpaRepository.findAll().stream()
                .filter(o -> o.getRestaurant().getRestaurantName().equals(restaurantEntity1().getRestaurantName()))
                .toList();

        //createDishesList
        var dishesList = List.of(
                buildDishesListEntity(fetchedDishes.get(0), orders.get(0)),
                buildDishesListEntity(fetchedDishes.get(0), orders.get(1)),
                buildDishesListEntity(fetchedDishes.get(1), orders.get(1))
        );
        dishesListJpaRepository.saveAllAndFlush(dishesList);
    }

    @Test
    void checkIfYouCanGetAllDishesBasedOnOrder() {
        //given
        var allOrders = supperOrderJpaRepository.findAll();
        var orders = allOrders.stream()
                .filter(o -> o.getRestaurant().getRestaurantName().equals(restaurantEntity1().getRestaurantName()))
                .toList();


        //when
        var dishes1 = dishesListJpaRepository.findAllByOrder(orders.get(0));
        var dishes2 = dishesListJpaRepository.findAllByOrder(orders.get(1));

        //then
        assertThat(dishes1).doesNotContainNull().hasSize(1);
        assertThat(dishes2).doesNotContainNull().hasSize(2);

    }

    //List<DishesListEntity> findAllByDish_DishId(Integer dishId);
    @Test
    void checkIfYouCanFindAllOrdersWithGivenDishId() {
        //given
        List<DishEntity> fetchedDishes = dishJpaRepository.findAll().stream()
                .filter(d -> d.getName().equals(dishEntity1().getName())
                        || d.getName().equals(dishEntity2().getName()))
                .toList();

        //when
        var dishesListEntities1 = dishesListJpaRepository.findAllByDish_DishId(fetchedDishes.get(0).getDishId());
        var dishesListEntities2 = dishesListJpaRepository.findAllByDish_DishId(fetchedDishes.get(1).getDishId());

        //then
        assertThat(dishesListEntities1).doesNotContainNull().hasSize(2);
        assertThat(dishesListEntities2).doesNotContainNull().hasSize(1);
    }
}