package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.Aevise.SupperSpeed.api.dto.OrderDTO;
import pl.Aevise.SupperSpeed.api.dto.StatusListDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.StatusListMapper;
import pl.Aevise.SupperSpeed.business.ProfileService;
import pl.Aevise.SupperSpeed.business.StatusListService;
import pl.Aevise.SupperSpeed.domain.SupperUser;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class OrdersBrowseController {

    private final  StatusListService statusListService;
    private final StatusListMapper statusListMapper;
    private final ProfileService profileService;
    private final SupperOrderService supperOrderService;

    static final String SUPPER_SPEED_ORDERS_BROWSER = "/orders";

    @GetMapping(SUPPER_SPEED_ORDERS_BROWSER)
    public String getOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
            ){
        List<StatusListDTO> statusList = getStatusList();
        Optional<SupperUser> user = profileService.findUserByEmail(userDetails.getUsername());

        if(user.isPresent()){
            List<OrderDTO> ordersByUserId = getOrdersByUserId(user.get().getSupperUserId());

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

    private List<OrderDTO> getOrdersByUserId(Integer userId){


        return List.of();
    }




}
