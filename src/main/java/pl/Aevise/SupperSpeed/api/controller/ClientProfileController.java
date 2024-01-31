package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.ClientMapper;
import pl.Aevise.SupperSpeed.business.ClientProfileViewService;
import pl.Aevise.SupperSpeed.business.UserProfileService;

@Controller
@AllArgsConstructor
public class ClientProfileController {

    static final String CLIENT_PROFILE = "/client/profile";

    private final ClientProfileViewService clientProfileViewService;
    private final UserProfileService userProfileService;
    private final ClientMapper clientMapper;
    private final AddressMapper addressMapper;


    @GetMapping(value = CLIENT_PROFILE)
    public String getClientProfile
            (
                    Model model,
                    @AuthenticationPrincipal UserDetails userDetails
            ) {

        ClientDTO clientDTO = clientProfileViewService
                .findByEmail(userDetails.getUsername())
                .map(clientMapper::map)
                .get();

        model.addAttribute("clientDTO", clientDTO);

        return "client_profile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute ClientDTO clientDTO) {

        return "redirect:/client/profile";
    }
}
