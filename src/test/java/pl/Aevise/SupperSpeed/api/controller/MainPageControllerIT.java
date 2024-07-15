package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.MainPageController.MAIN_PAGE;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.testPassword;
import static pl.Aevise.SupperSpeed.util.FlywayConstants.flywayCuisines;
import static pl.Aevise.SupperSpeed.util.FlywayConstants.flywayDistinctCities;

@AutoConfigureMockMvc
class MainPageControllerIT extends AbstractITConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    public static Stream<Arguments> checkThatYouGetProfileButtonWhenLogged() {
        return Stream.of(
                Arguments.of("CLIENT", "<a class=\"btn btn-info float-end\" role=\"button\" href=\"/client/profile\">Profile</a>"),
                Arguments.of("RESTAURANT", "<a class=\"btn btn-info float-end\" role=\"button\" href=\"/restaurant/profile\">Profile</a>")
        );
    }

    @Test
    void checkThatYouCanGetMainPage() {
        //given
        String url = String.format("http://localhost:%s%s", port, basePath);

        //when
        ResponseEntity<String> page = testRestTemplate.getForEntity(url, String.class);
        String content = page.getBody();

        //then
        assertThat(content).contains("To order please provide your address:");
        assertThat(content).contains(flywayCuisines);
        assertThat(content).contains(flywayDistinctCities);
        assertTrue(page.getStatusCode().is2xxSuccessful());
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouGetProfileButtonWhenLogged(
            String authority,
            String expectedPartOfHTML
    ) throws Exception {
        //given
        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_1).password(testPassword).authorities(authority).build();

        //when
        ResultActions result = mockMvc.perform(get(MAIN_PAGE)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)));
        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("distinctCities"))
                .andExpect(model().attributeExists("cuisines"))
                .andExpect(model().attributeExists("userRole"))
                .andExpect(view().name("main_page"));

        assertThat(content).contains(expectedPartOfHTML);
        assertThat(content).contains(flywayCuisines);
        assertThat(content).contains(flywayDistinctCities);
    }
}