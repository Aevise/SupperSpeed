package pl.Aevise.SupperSpeed.api.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.Aevise.SupperSpeed.api.dto.StatusListDTO;
import pl.Aevise.SupperSpeed.api.dto.SupperOrderDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.StatusListMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.SupperOrderMapper;
import pl.Aevise.SupperSpeed.business.ProfileService;
import pl.Aevise.SupperSpeed.business.StatusListService;
import pl.Aevise.SupperSpeed.business.SupperOrderService;
import pl.Aevise.SupperSpeed.domain.SupperUser;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@AllArgsConstructor
public class OrdersBrowseController {

    private final  StatusListService statusListService;
    private final StatusListMapper statusListMapper;
    private final ProfileService profileService;
    private final SupperOrderService supperOrderService;
    private final SupperOrderMapper supperOrderMapper;

    static final String SUPPER_SPEED_ORDERS_BROWSER = "/orders";

    @GetMapping(SUPPER_SPEED_ORDERS_BROWSER)
    public String getOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
            ){
        List<StatusListDTO> statusList = getStatusList();
        Optional<SupperUser> user = profileService.findUserByEmail(userDetails.getUsername());
        List<SupperOrderDTO> ordersByUserId = new ArrayList<>();

        if(user.isPresent()){
            try{
                ordersByUserId = getOrdersByUserIdAndAuthority(
                        user.get().getSupperUserId(),
                        userDetails
                                .getAuthorities()
                                .stream()
                                .findFirst()
                                .map(GrantedAuthority::getAuthority)
                                .orElseThrow(()->
                                        new EntityNotFoundException(
                                                "User does not exists"
                                        )));
            }catch (InvalidDataAccessResourceUsageException ex){
                log.warn("Orders not found for user [{}]", user.get().getSupperUserId());
            }

            model.addAttribute("statusListDTO", statusList);
            model.addAttribute("ordersDTO", ordersByUserId);
        }


        return "orders_page";
    }


    private  List<StatusListDTO> getStatusList(){
        return statusListService.getStatusList()
                .stream()
                .map(statusListMapper::mapToDTO)
                .toList();
    }

    private List<SupperOrderDTO> getOrdersByUserIdAndAuthority(Integer userId, String authority){
        if (authority.equalsIgnoreCase(AvailableRoles.RESTAURANT.toString())){
            return supperOrderService
                    .getOrdersByRestaurantId(userId)
                    .stream()
                    .map(supperOrderMapper::mapToDTO)
                    .toList();
        }
        return supperOrderService
                .getOrdersByClientId(userId)
                .stream()
                .map(supperOrderMapper::mapToDTO)
                .toList();
    }




}
