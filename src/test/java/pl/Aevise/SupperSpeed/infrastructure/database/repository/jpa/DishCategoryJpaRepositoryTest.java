package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.integration.configuration.PersistenceContainerTestConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.Aevise.SupperSpeed.util.Constants.DISH_CATEGORY;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.restaurantEntity1;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DishCategoryJpaRepositoryTest {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final DishCategoryJpaRepository dishCategoryJpaRepository;

    private static DishCategoryEntity buildDishCategory(RestaurantEntity restaurant, String categoryName) {
        return DishCategoryEntity.builder()
                .restaurant(restaurant)
                .categoryName(categoryName)
                .build();
    }

    @Test
    void checkThatYouCanFetchAllDishCategoriesForRestaurant() {
        //given
        var restaurant = restaurantJpaRepository.saveAndFlush(restaurantEntity1());
        var dishCategories = List.of(
                buildDishCategory(restaurant, DISH_CATEGORY.get("Meat")),
                buildDishCategory(restaurant, DISH_CATEGORY.get("Vegan"))
        );
        dishCategoryJpaRepository.saveAllAndFlush(dishCategories);

        //when
        var fetchedDishCategories = dishCategoryJpaRepository.findAllByRestaurant_Id(restaurant.getId());

        //then
        assertThat(fetchedDishCategories).doesNotContainNull().hasSize(2);
    }

    @Test
    void checkThatYouCanGetDishCategoryByName() {
        //given
        String categoryName = "Dania rybne";

        //when
        Optional<DishCategoryEntity> byCategoryName = dishCategoryJpaRepository.findByCategoryName(categoryName);

        //then
        assertTrue(byCategoryName.isPresent());
        assertEquals(byCategoryName.get().getCategoryName(), categoryName);
    }
}