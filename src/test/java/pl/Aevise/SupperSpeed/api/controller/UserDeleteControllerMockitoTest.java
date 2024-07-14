package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import pl.Aevise.SupperSpeed.business.UserService;

import java.util.stream.Stream;

import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.testPassword;

@ExtendWith(MockitoExtension.class)
class UserDeleteControllerMockitoTest {
    @InjectMocks
    UserDeleteController userDeleteController;
    @Mock
    private UserService userService;

    private static Stream<Arguments> checkThatYouCanCorrectlyDeleteUser() {
        return Stream.of(
                Arguments.of("yes", "CLIENT", "redirect:/logout"),
                Arguments.of("yes", "RESTAURANT", "redirect:/logout")
        );
    }

    private static Stream<Arguments> checkThatYouAreCorrectlyRedirectedIfYouDoNotDeleteUser() {
        return Stream.of(
                Arguments.of("no", "RESTAURANT", "redirect:/restaurant/profile"),
                Arguments.of("no", "CLIENT", "redirect:/client/profile")
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanCorrectlyDeleteUser(String confirmation, String authority, String expectedResult) {
        //given
        UserDetails userDetails = User.withUsername(TEST_CLIENT_EMAIL_1).password(testPassword).authorities(authority).build();

        //when
        Mockito.doNothing().when(userService).deleteUserByEmail(userDetails.getUsername());

        String result = userDeleteController.deleteUser(userDetails, confirmation);

        //then
        Assertions.assertEquals(result, expectedResult);
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouAreCorrectlyRedirectedIfYouDoNotDeleteUser(String confirmation, String authority, String expectedResult) {
        //given
        UserDetails userDetails = User.withUsername(TEST_CLIENT_EMAIL_1).password(testPassword).authorities(authority).build();

        //when
        String result = userDeleteController.deleteUser(userDetails, confirmation);

        //then
        Assertions.assertEquals(result, expectedResult);
    }
}