package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.controller.utils.PaginationAndSortingUtils;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.DeliveryAddressDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.DeliveryAddressService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;

import java.util.List;

@Controller
@AllArgsConstructor
public class DeliveryAddressesController {
    private final String SHOW_DELIVERY_ADDRESSES = "/restaurant/delivery-addresses";
    private final String REMOVE_DELIVERY_ADDRESS = "/restaurant/delivery-addresses/remove";
    private final String ADD_DELIVERY_ADDRESS = "/restaurant/delivery-addresses/add";

    private final DeliveryAddressService deliveryAddressService;

    private final AddressService addressService;

    private final RestaurantService restaurantService;

    @GetMapping(SHOW_DELIVERY_ADDRESSES)
    public String showDeliveryAddresses(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "dir", required = false) String sortingDirection
    ) {
        RestaurantDTO restaurantDTO = restaurantService
                .findRestaurantByEmail(
                        userDetails.getUsername());
        System.out.println("gmm");
        Integer restaurantId = restaurantDTO.getRestaurantId();


        AddressDTO restaurantAddress = addressService.getByRestaurantId(restaurantId);

        if (sortingDirection == null ||
                (!sortingDirection.equals(PaginationAndSortingUtils.ASC.name())
                        && !sortingDirection.equals(PaginationAndSortingUtils.DESC.name()))) {
            sortingDirection = PaginationAndSortingUtils.ASC.name();
            page = 0;
        }
        Page<DeliveryAddressList> allDeliveryAddressesListByRestaurantId = deliveryAddressService.getAllDeliveryAddressesByRestaurantId(
                restaurantId,
                buildPageRequest(
                        sortingDirection,
                        page
                ));

        List<DeliveryAddressDTO> allDeliveryAddressesByRestaurantId = deliveryAddressService.separateAddresses(allDeliveryAddressesListByRestaurantId);


        List<DeliveryAddressDTO> addressesWithoutDelivery = deliveryAddressService
                .getAddressesWithoutDeliveryBasedOnPostalCode(restaurantId,
                        buildDeliveryAddressFromRestaurantAddress(restaurantAddress));

        model.addAttribute("addresses", allDeliveryAddressesByRestaurantId);
        model.addAttribute("restaurantAddress", restaurantAddress);
        model.addAttribute("addressesWithoutDelivery", addressesWithoutDelivery);
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("totalNumberOfPages", allDeliveryAddressesListByRestaurantId.getTotalPages());
        model.addAttribute("sortingDirection", sortingDirection);
        return "delivery_addresses";
    }

    @PostMapping(REMOVE_DELIVERY_ADDRESS)
    public String removeDeliveryAddress(
            @RequestParam(name = "deliveryAddressId") Integer deliveryAddressId,
            @RequestParam(name = "restaurantId") Integer restaurantId
    ) {
        deliveryAddressService.removeDeliveryAddress(deliveryAddressId, restaurantId);

        return "redirect:" + SHOW_DELIVERY_ADDRESSES;
    }

    @PostMapping(ADD_DELIVERY_ADDRESS)
    public String addDeliveryAddress(
            DeliveryAddressDTO deliveryAddressDTO,
            @RequestParam(name = "restaurantId") Integer restaurantId
    ) {

        deliveryAddressService.addDeliveryAddress(deliveryAddressDTO, restaurantId);
        return "redirect:" + SHOW_DELIVERY_ADDRESSES;
    }


    private DeliveryAddressDTO buildDeliveryAddressFromRestaurantAddress(AddressDTO addressDTO) {
        return DeliveryAddressDTO.builder()
                .city(addressDTO.getCity())
                .country(addressDTO.getCountry())
                .postalCode(addressDTO.getPostalCode())
                .streetName(addressDTO.getStreetName())
                .build();
    }

    private PageRequest buildPageRequest(String direction, Integer page) {
        if (direction.equals(PaginationAndSortingUtils.ASC.name())) {
            return PageRequest.of(page, 10, Sort.by("deliveryAddressEntity.streetName").ascending());
        }
        return PageRequest.of(page, 10, Sort.by("deliveryAddressEntity.streetName").descending());
    }

}
