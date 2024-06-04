package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.DeliveryAddressDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.DeliveryAddressService;
import pl.Aevise.SupperSpeed.business.RestaurantService;

import java.util.List;

@Controller
@AllArgsConstructor
public class DeliveryAddressesController {
    private final String SHOW_DELIVERY_ADDRESSES = "/restaurant/delivery-addresses";
    private final String DELETE_DELIVERY_ADDRESS = "/restaurant/delivery-addresses/delete";
    private final String ADD_DELIVERY_ADDRESS = "/restaurant/delivery-addresses/add";

    private final DeliveryAddressService deliveryAddressService;

    private final AddressService addressService;

    private final RestaurantService restaurantService;

    @GetMapping(SHOW_DELIVERY_ADDRESSES)
    public String showDeliveryAddresses(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ){
        RestaurantDTO restaurantDTO = restaurantService
                .findRestaurantByEmail(
                        userDetails.getUsername());
        Integer restaurantId = restaurantDTO.getRestaurantId();

        List<DeliveryAddressDTO> allDeliveryAddressesByRestaurantId = deliveryAddressService.getAllDeliveryAddressesByRestaurantId(restaurantId);
        AddressDTO restaurantAddress = addressService.getByRestaurantId(restaurantId);

        List<DeliveryAddressDTO> addressesWithoutDelivery = deliveryAddressService.getAddressesWithoutDeliveryBasedOnPostalCode(restaurantId, restaurantAddress.getPostalCode());

        model.addAttribute("addresses", allDeliveryAddressesByRestaurantId);
        model.addAttribute("restaurantAddress", restaurantAddress);
        model.addAttribute("addressesWithoutDelivery", addressesWithoutDelivery);
        model.addAttribute("restaurantId", restaurantId);
        return "delivery_addresses";
    }

    @PostMapping(DELETE_DELIVERY_ADDRESS)
    public String deleteDeliveryAddress(
            @RequestParam(name = "deliveryAddressId") Integer deliveryAddressId,
            @RequestParam(name = "restaurantId") Integer restaurantId
    ){
        deliveryAddressService.deleteDeliveryAddressById(deliveryAddressId, restaurantId);

        return "redirect:" + SHOW_DELIVERY_ADDRESSES;
    }

    @PostMapping(ADD_DELIVERY_ADDRESS)
    public String addDeliveryAddress(
            DeliveryAddressDTO deliveryAddressDTO,
            @RequestParam(name = "restaurantId") Integer restaurantId
    ){

        deliveryAddressService.addDeliveryAddress(deliveryAddressDTO, restaurantId);
        return "redirect:" + SHOW_DELIVERY_ADDRESSES;
    }


}
