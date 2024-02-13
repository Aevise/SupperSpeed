package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.business.UserService;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import java.util.Collection;
import java.util.List;
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

    @GetMapping(value = CLIENT_DELETE)
    public String deletePage(){
        return "delete";
    }

    @PostMapping(value = CLIENT_DELETE)
    public String deleteUser
            (
                    @AuthenticationPrincipal UserDetails userDetails,
                    @RequestParam(required = false) String confirmation
            ){
        if("yes".equals(confirmation)){
            userService.deleteUserByEmail(userDetails.getUsername());
            return "redirect:" + CLIENT_LOGOUT;
        }

        var grantedAuthorities = getUsersAuthorities(userDetails);


        if(grantedAuthorities.contains(AvailableRoles.CLIENT.name())){
            return "redirect:" + CLIENT_PROFILE;
        }
        return "redirect:" + RESTAURANT_PROFILE;
    }

    private static Set<String> getUsersAuthorities(UserDetails userDetails) {
        return userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

}
