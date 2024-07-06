package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.CuisineService;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles.RESTAURANT;
import static pl.Aevise.SupperSpeed.util.Constants.LUBLIN;
import static pl.Aevise.SupperSpeed.util.Constants.WARSZAWA;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.cuisineDTO1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.cuisineDTO2;

@ExtendWith(MockitoExtension.class)
class MainPageControllerMockitoTest {
    @Mock
    private AddressService addressService;
    @Mock
    private CuisineService cuisineService;
    @Mock
    private SecurityService securityService;

    @InjectMocks
    private MainPageController mainPageController;

    @Test
    void checkThatYouCanGetMainPage() {
        //given
        var cities = List.of(WARSZAWA, LUBLIN);
        var cuisines = List.of(cuisineDTO1(), cuisineDTO2());
        String userRole = RESTAURANT.name();

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(securityService.getUserAuthority()).thenReturn(userRole);
        when(addressService.findDistinctCities()).thenReturn(cities);
        when(cuisineService.findAllSorted("asc")).thenReturn(cuisines);

        String result = mainPageController.getMainPage(model);

        //then
        assertThat(result).isNotNull().isEqualTo("main_page");
        assertThat(model.get("distinctCities")).isNotNull().isEqualTo(cities);
        assertThat(model.get("cuisines")).isNotNull().isEqualTo(cuisines);
        assertThat(model.get("userRole")).isNotNull().isEqualTo(userRole);
    }
}