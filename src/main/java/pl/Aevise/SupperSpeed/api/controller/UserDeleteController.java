package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.business.UserService;

@Controller
@AllArgsConstructor
public class UserDeleteController {

    static final String CLIENT_DELETE = "/delete";
    static final String CLIENT_LOGOUT = "/logout";
    static final String CLIENT_PROFILE = "/client/profile";

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
        return "redirect:" + CLIENT_PROFILE;
    }

}
