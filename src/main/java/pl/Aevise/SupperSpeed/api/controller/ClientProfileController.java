package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.ClientMapper;
import pl.Aevise.SupperSpeed.business.ClientProfileViewService;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.domain.SupperUser;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class ClientProfileController {

    static final String CLIENT_PROFILE = "/client/profile";

    private final ClientProfileViewService clientProfileViewService;
    private final ClientMapper clientMapper;


    @GetMapping(value = CLIENT_PROFILE)
    public String getClientProfile
            (
                    Model model,
                    @AuthenticationPrincipal UserDetails userDetails
            ) {

        Optional<ClientDTO> clientDTO = clientProfileViewService
                .findByEmail(userDetails.getUsername())
                .map(clientMapper::map);

        model.addAttribute("clientDTO", clientDTO);

        return "client_profile";
    }
}
