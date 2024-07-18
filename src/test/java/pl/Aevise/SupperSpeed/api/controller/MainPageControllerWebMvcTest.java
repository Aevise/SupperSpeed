package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.api.controller.utils.PaginationAndSortingUtils;
import pl.Aevise.SupperSpeed.api.dto.CuisineDTO;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.CuisineService;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;
import pl.Aevise.SupperSpeed.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.MainPageController.MAIN_PAGE;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.cuisineDTO1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.cuisineDTO2;

@WebMvcTest(controllers = MainPageController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser
class MainPageControllerWebMvcTest {

    private final MockMvc mockMvc;

    @MockBean
    private AddressService addressService;
    @MockBean
    private CuisineService cuisineService;
    @MockBean
    private SecurityService securityService;

    public static Stream<Arguments> checkThatProfileButtonAppearsForLoggedUser() {
        return Stream.of(
                Arguments.of("CLIENT", "<a class=\"btn btn-info float-end\" role=\"button\" href=\"/client/profile\">Profile</a>"),
                Arguments.of("RESTAURANT", "<a class=\"btn btn-info float-end\" role=\"button\" href=\"/restaurant/profile\">Profile</a>")
        );
    }

    @Test
    void checkThatYouCanGetMainPage() throws Exception {
        //given
        List<String> cities = List.of(Constants.LUBLIN, Constants.WARSZAWA);
        List<CuisineDTO> cuisines = List.of(cuisineDTO1(), cuisineDTO2());
        String role = "anonymousUser";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("userRole", role);
        cuisines.forEach(cuisine -> params.add("cuisines", cuisine.toString()));
        cities.forEach(city -> params.add("distinctCities", city));
        parametersMap.forEach(params::add);

        //when
        when(securityService.getUserAuthority()).thenReturn(role);
        when((addressService.findDistinctCities())).thenReturn(cities);
        when(cuisineService.findAllSorted(PaginationAndSortingUtils.ASC.getSortingDirection())).thenReturn(cuisines);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(MAIN_PAGE)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("distinctCities"))
                .andExpect(model().attribute("distinctCities", cities))
                .andExpect(model().attributeExists("cuisines"))
                .andExpect(model().attribute("cuisines", cuisines))
                .andExpect(model().attributeExists("userRole"))
                .andExpect(view().name("main_page"));
    }

    @Test
    public void checkThatYouGetProperInformationIfNoRestaurantsAreAvailableYet() throws Exception {
        //given
        List<CuisineDTO> cuisines = List.of(cuisineDTO1(), cuisineDTO2());
        String role = "anonymousUser";
        String expectedPartOfHTML = "No restaurant available yet!";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("userRole", role);
        cuisines.forEach(cuisine -> params.add("cuisines", cuisine.toString()));
        parametersMap.forEach(params::add);

        //when
        when(securityService.getUserAuthority()).thenReturn(role);
        when((addressService.findDistinctCities())).thenReturn(List.of());
        when(cuisineService.findAllSorted(PaginationAndSortingUtils.ASC.getSortingDirection())).thenReturn(cuisines);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(MAIN_PAGE)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));
        String content = result.andReturn().getResponse().getContentAsString();


        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("distinctCities"))
                .andExpect(model().attribute("distinctCities", List.of()))
                .andExpect(model().attributeExists("cuisines"))
                .andExpect(model().attribute("cuisines", cuisines))
                .andExpect(model().attributeExists("userRole"))
                .andExpect(view().name("main_page"));
        assertThat(content).contains(expectedPartOfHTML);
    }

    @ParameterizedTest
    @MethodSource
    void checkThatProfileButtonAppearsForLoggedUser(
            String authentication,
            String expectedPartOfHTML
    ) throws Exception {
        //given
        List<String> cities = List.of(Constants.LUBLIN, Constants.WARSZAWA);
        List<CuisineDTO> cuisines = List.of(cuisineDTO1(), cuisineDTO2());
        UserDetails userDetails = User.withUsername(authentication).password(authentication).authorities(authentication).build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("userRole", authentication);
        cuisines.forEach(cuisine -> params.add("cuisines", cuisine.toString()));
        cities.forEach(city -> params.add("distinctCities", city));
        parametersMap.forEach(params::add);

        //when
        when(securityService.getUserAuthority()).thenReturn(authentication);
        when((addressService.findDistinctCities())).thenReturn(cities);
        when(cuisineService.findAllSorted(PaginationAndSortingUtils.ASC.getSortingDirection())).thenReturn(cuisines);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(MAIN_PAGE)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)));
        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("distinctCities"))
                .andExpect(model().attribute("distinctCities", cities))
                .andExpect(model().attributeExists("cuisines"))
                .andExpect(model().attribute("cuisines", cuisines))
                .andExpect(model().attributeExists("userRole"))
                .andExpect(view().name("main_page"));
        assertThat(content).contains(expectedPartOfHTML);
    }
}