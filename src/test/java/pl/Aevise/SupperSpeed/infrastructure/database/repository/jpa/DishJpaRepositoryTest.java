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
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
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
class DishJpaRepositoryTest {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final DishCategoryJpaRepository dishCategoryJpaRepository;

    private final DishJpaRepository dishJpaRepository;

    @BeforeEach
    void createDishes() {
        //create restaurant
        RestaurantEntity restaurant = restaurantJpaRepository.save(restaurantEntity1());

        //create categories
        var dishCategories = List.of(DishCategoryEntity.builder()
                        .categoryName("test1")
                        .restaurant(restaurant)
                        .build(),
                DishCategoryEntity.builder()
                        .categoryName("test2")
                        .restaurant(restaurant)
                        .build());
        var categoryEntities = dishCategoryJpaRepository.saveAllAndFlush(dishCategories);


        //create dishes
        var dishes = List.of(
                dishEntity1(),
                dishEntity2(),
                dishEntity3()
        );
        for (DishEntity dish : dishes) {
            dish.setRestaurant(restaurant);
            dish.setDishCategory(categoryEntities.get(0));
        }
        dishes.get(2).setDishCategory(categoryEntities.get(1));
        dishJpaRepository.saveAllAndFlush(dishes);
    }


    //    List<DishEntity> findAllByRestaurant_Id(Integer restaurantId);
    @Test
    void checkThatYouCanGetAllDishesPreparedByRestaurant() {
        //given
        List<RestaurantEntity> all = restaurantJpaRepository.findAll();
        RestaurantEntity restaurant = all.stream()
                .filter(r -> r.getRestaurantName().equals(restaurantEntity1().getRestaurantName()))
                .findFirst()
                .orElseThrow();

        //when
        List<DishEntity> dishes1 = dishJpaRepository.findAllByRestaurant_Id(restaurant.getId());
        List<DishEntity> dishes2 = dishJpaRepository.findAllByRestaurant_Id(999999999);

        //then
        assertThat(dishes1).doesNotContainNull().hasSize(3)
                .containsExactlyInAnyOrder(dishEntity1(), dishEntity2(), dishEntity3());
        assertThat(dishes2).hasSize(0);
    }

    @Test
    void checkThatYouCanGetAllDishesBasedOnCategoryId() {
        //given
        var categories = dishCategoryJpaRepository.findAll().stream()
                .filter(c -> c.getCategoryName().equals("test1")
                        || c.getCategoryName().equals("test2"))
                .toList();
        var dishesWithTest1Category = 2;
        var dishesWithTest2Category = 1;


        //when
        List<DishEntity> dishes1 = dishJpaRepository.findAllByDishCategory_DishCategoryId(categories.get(0).getDishCategoryId());
        List<DishEntity> dishes2 = dishJpaRepository.findAllByDishCategory_DishCategoryId(categories.get(1).getDishCategoryId());

        //then
        assertThat(dishes1).doesNotContainNull().hasSize(dishesWithTest1Category);
        assertThat(dishes2).doesNotContainNull().hasSize(dishesWithTest2Category);
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanGetAllDishesBasedOnRestaurantName(
            String restaurantName,
            int expectedAmountOfDishes
    ){
        //given, when
        List<DishEntity> dishes = dishJpaRepository.findAllByRestaurant_RestaurantNameAndRestaurant_IsShownAndIsHidden(restaurantName, true, false);

        //then
        assertThat(dishes).hasSize(expectedAmountOfDishes);
    }

    public static Stream<Arguments> checkThatYouCanGetAllDishesBasedOnRestaurantName(){
        return Stream.of(
                Arguments.of("restaurant1", 0),
                Arguments.of("restaurant2", 2),
                Arguments.of("restaurant3", 5)
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanGetDishesBasedByRestaurantNameAndDishCategory(
            String restaurantName,
            String categoryName,
            Integer expectedAmountOfDishes
    ){
        //given, when
        List<DishEntity> dishes = dishJpaRepository.findAllByRestaurant_RestaurantNameAndRestaurant_IsShownAndDishCategory_CategoryNameAndIsHidden(
                restaurantName, true, categoryName, false);

        //then
        assertThat(dishes).hasSize(expectedAmountOfDishes);
    }

    public static Stream<Arguments> checkThatYouCanGetDishesBasedByRestaurantNameAndDishCategory() {
        return Stream.of(
                Arguments.of("restaurant3", "Dania rybne", 2),
                Arguments.of("restaurant3", "Dania mięsne", 2),
                Arguments.of("restaurant3", "Dania wegańskie", 1)
                );
    }
}