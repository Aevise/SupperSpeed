package pl.Aevise.SupperSpeed.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.ClientMapper;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.ClientProfileService;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.domain.exception.NotFoundException;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class ClientProfileController {

    static final String CLIENT_PROFILE = "/client/profile";
    static final String UPDATE_PROFILE = "/client/profile/update";

    private final ClientProfileService clientProfileService;
    private final AddressService addressService;
    private final ClientMapper clientMapper;
    private final AddressMapper addressMapper;


    @GetMapping(value = CLIENT_PROFILE)
    public String getClientProfile
            (
                    Model model,
                    @AuthenticationPrincipal UserDetails userDetails
            ) {

        Optional<Client> client = clientProfileService
                .findClientByEmail(userDetails.getUsername());

        if (client.isPresent()) {
            ClientDTO clientDTO = clientMapper.mapToDTO(client.get());

            AddressDTO addressDTO = addressService
                    .findById(client.get().getAddress().getAddressId())
                    .map(addressMapper::mapToDTO)
                    .orElse(new AddressDTO());

            model.addAttribute("clientDTO", clientDTO);
            model.addAttribute("addressDTO", addressDTO);
            model.addAttribute("clientId", clientDTO.getId());
            return "client_profile";
        }
        throw new NotFoundException(Client.class.toString());
    }

    @PostMapping(UPDATE_PROFILE)
    public String updateProfile
            (
                    @Valid @ModelAttribute("clientDTO") ClientDTO clientDTO,
                    @ModelAttribute("addressDTO") AddressDTO addressDTO,
                    @RequestParam String clientId,
                    @RequestParam(required = false) String action
            ) {

        var authority = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().stream()
                .findFirst().orElseThrow(()-> new AccessDeniedException("You do not have the required authority to view this page."))
                .getAuthority();
        if (!authority.equals(AvailableRoles.CLIENT.name())) {
            return "redirect:/";
        }

        if ("updateAddress".equals(action)) {
            clientProfileService.updateAddress(addressDTO, Integer.valueOf(clientId));
        } else {
            clientProfileService.updateClientInformation(clientDTO, Integer.valueOf(clientId));
        }
        return "redirect:" + CLIENT_PROFILE;
    }
}
