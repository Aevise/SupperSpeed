package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ExtendedModelMap;
import pl.Aevise.SupperSpeed.api.controller.utils.interfaces.PageRequestUtils;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.business.UserRatingService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;

@ExtendWith(MockitoExtension.class)
class OpinionControllerMockitoTest {
    @Mock
    private UserRatingService userRatingService;
    @Mock
    private RestaurantService restaurantService;
    @Mock
    private PageRequestUtils pageRequestUtils;

    @InjectMocks
    private OpinionController opinionController;

    @Test
    void showOpinionsAboutRestaurant() {
        //given
        RestaurantDTO restaurant = restaurantDTO1();
        Integer restaurantId = restaurant.getRestaurantId();
        var opinions = new PageImpl<>(List.of(opinionDTO1()));
        String currDir = "asc";
        int page = 0;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("orderId").ascending());

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(restaurantService.findRestaurantDTOById(restaurantId)).thenReturn(restaurant);
        when(pageRequestUtils.buildPageRequestForRatedOrders(currDir, page)).thenReturn(pageRequest);
        when(userRatingService.getOpinionsAboutOrdersFromRestaurant(restaurantId, pageRequest)).thenReturn(opinions);
        when(userRatingService.getRestaurantRating(restaurantId)).thenReturn(totalRestaurantRatingDTO1());

        String result = opinionController.showOpinionsAboutRestaurant(model, restaurantId, currDir, page);

        //then
        assertThat(result).isNotNull().isEqualTo("opinion");
        assertThat(model.get("opinions")).isNotNull().isEqualTo(opinions.toList());
        assertThat(model.get("restaurant")).isNotNull().isEqualTo(restaurant);
        assertThat(model.get("totalRating")).isNotNull().isEqualTo(totalRestaurantRatingDTO1());
        assertThat(model.get("currDir")).isNotNull().isEqualTo(currDir);
        assertThat(model.get("currPage")).isNotNull().isEqualTo(page);
        assertThat(model.get("totalNumberOfPages")).isNotNull().isEqualTo(opinions.getTotalPages());
    }
}