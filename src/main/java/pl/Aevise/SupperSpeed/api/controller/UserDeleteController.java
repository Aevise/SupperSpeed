package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.business.UserService;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class UserDeleteController {

    static final String CLIENT_DELETE = "/delete";
    static final String CLIENT_LOGOUT = "/logout";
    static final String CLIENT_PROFILE = "/client/profile";
    static final String RESTAURANT_PROFILE = "/restaurant/profile";

    private final UserService userService;

    private static Set<String> getUsersAuthorities(UserDetails userDetails) {
        return userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = CLIENT_DELETE)
    public String deletePage() {
        var authority = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().stream().findFirst()
                .orElseThrow(() -> new AccessDeniedException("You do not have the required authority to access this page."))
                .getAuthority();

        if (authority.equals(AvailableRoles.RESTAURANT.name()) || authority.equals(AvailableRoles.CLIENT.name())) {
            return "delete";
        }
        throw new AccessDeniedException("You do not have the required authority to access this page.");
    }

    @PostMapping(value = CLIENT_DELETE)
    public String deleteUser
            (
                    @AuthenticationPrincipal UserDetails userDetails,
                    @RequestParam(required = false) String confirmation
            ) {
        var grantedAuthorities = getUsersAuthorities(userDetails);
        String authority = grantedAuthorities.stream().findFirst().orElseThrow(
                () -> new AccessDeniedException("You do not have the required authority to access this page.")
        );
        if ("yes".equals(confirmation) &&
                (authority.equals(AvailableRoles.CLIENT.name()) || authority.equals(AvailableRoles.RESTAURANT.name()))) {
            userService.deleteUserByEmail(userDetails.getUsername(), authority);
            return "redirect:" + CLIENT_LOGOUT;
        }

        if (authority.equals(AvailableRoles.CLIENT.name())) {
            return "redirect:" + CLIENT_PROFILE;
        }
        return "redirect:" + RESTAURANT_PROFILE;
    }

}
