package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
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
import pl.Aevise.SupperSpeed.business.UserService;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.UserDeleteController.*;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.testPassword;

@WebMvcTest(controllers = UserDeleteController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class UserDeleteControllerMockMvcTest {

    private final MockMvc mockMvc;

    @MockBean
    private final UserService userService;

    public static Stream<String> checkThatYouCanGetDeletePage() {
        return Stream.of("RESTAURANT", "CLIENT");
    }

    public static Stream<Arguments> checkThatYouAuthorizedUserCanDeleteProfile() {
        return Stream.of(
                Arguments.of("RESTAURANT", "yes"),
                Arguments.of("CLIENT", "yes")
        );
    }

    public static Stream<Arguments> checkThatYouAreRedirectedToCorrectProfileWhenDenyingProfileDeletion() {
        return Stream.of(
                Arguments.of("RESTAURANT", "no", "redirect:" + RESTAURANT_PROFILE),
                Arguments.of("CLIENT", "no", "redirect:" + CLIENT_PROFILE)
        );
    }

    @Test
    @WithMockUser
    void checkThatExceptionIsThrownWhenYouTryToAccessDeletePageWithoutAuthority() throws Exception {
        //given
        String authority = "fail";
        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_1).password(testPassword).authorities(authority).build();
        String expectedErrorMessage = "Error:\n[You do not have the required authority to access this page.]";

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(CLIENT_DELETE)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)));

        //then
        result
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", expectedErrorMessage))
                .andExpect(view().name("error"));
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanGetDeletePage(
            String authority
    ) throws Exception {
        //given
        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_1).password(testPassword).authorities(authority).build();

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(CLIENT_DELETE)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("delete"));
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouAuthorizedUserCanDeleteProfile(
            String authority,
            String deleteProfile
    ) throws Exception {
        //given
        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_1).password(testPassword).authorities(authority).build();

        //when
        Mockito.doNothing().when(userService).deleteUserByEmail(TEST_RESTAURANT_EMAIL_1, authority);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(CLIENT_DELETE)
                .param("confirmation", deleteProfile)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + CLIENT_LOGOUT));

    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouAreRedirectedToCorrectProfileWhenDenyingProfileDeletion(
            String authority,
            String deleteProfile,
            String redirectURL
    ) throws Exception {
        //given
        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_1).password(testPassword).authorities(authority).build();

        //when
        Mockito.doNothing().when(userService).deleteUserByEmail(TEST_RESTAURANT_EMAIL_1, authority);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(CLIENT_DELETE)
                .param("confirmation", deleteProfile)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(redirectURL));
    }
}