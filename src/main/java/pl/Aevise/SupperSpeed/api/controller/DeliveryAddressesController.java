package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.DeliveryAddressDTO;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.DeliveryAddressService;

import java.util.List;

@Controller
@AllArgsConstructor
public class DeliveryAddressesController {
    private final String SHOW_DELIVERY_ADDRESSES = "/restaurant/delivery-addresses";

    private final DeliveryAddressService deliveryAddressService;
    private final AddressService addressService;

    @GetMapping(SHOW_DELIVERY_ADDRESSES)
    public String showDeliveryAddresses(
            @RequestParam Integer restaurantId,
            Model model
    ){
        //TODO TUTAJ NIE MOZE BYC WYSZUKIWANIE STRONY PO RESTAURANTID!!!!!
        List<DeliveryAddressDTO> allDeliveryAddressesByRestaurantId = deliveryAddressService.getAllDeliveryAddressesByRestaurantId(restaurantId);
        AddressDTO restaurantAddress = addressService.getByRestaurantId(restaurantId);

        model.addAttribute("addresses", allDeliveryAddressesByRestaurantId);
        model.addAttribute("restaurantAddress", restaurantAddress);
        return "delivery_addresses";
    }
}
