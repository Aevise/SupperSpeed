package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import java.util.List;

@Controller
@AllArgsConstructor
public class DeliveryAddressesController {
    public static final String SHOW_DELIVERY_ADDRESSES = "/restaurant/profile/delivery-addresses";
    public static final String REMOVE_DELIVERY_ADDRESS = "/restaurant/profile/delivery-addresses/remove";
    public static final String ADD_DELIVERY_ADDRESS = "/restaurant/profile/delivery-addresses/add";

    private final DeliveryAddressService deliveryAddressService;

    private final AddressService addressService;

    private final RestaurantService restaurantService;

    @GetMapping(SHOW_DELIVERY_ADDRESSES)
    public String showDeliveryAddresses(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            @RequestParam(value = "ca-page", required = false, defaultValue = "0") Integer currAdrPage,
            @RequestParam(value = "ca-dir", required = false, defaultValue = "asc") String currAdrSortingDirection,
            @RequestParam(value = "ea-page", required = false, defaultValue = "0") Integer exiAdrPage,
            @RequestParam(value = "ea-dir", required = false, defaultValue = "asc") String exiAdrSortingDirection
    ) {
        RestaurantDTO restaurantDTO = restaurantService
                .findRestaurantByEmail(
                        userDetails.getUsername());
        Integer restaurantId = restaurantDTO.getRestaurantId();

        AddressDTO restaurantAddress = addressService.getByRestaurantId(restaurantId);

        Page<DeliveryAddressList> allDeliveryAddressesListByRestaurantId = deliveryAddressService.getAllDeliveryAddressesByRestaurantId(
                restaurantId,
                buildPageRequestForDeliveryAddressList(
                        currAdrSortingDirection,
                        currAdrPage
                ));
        List<DeliveryAddressDTO> allDeliveryAddressesByRestaurantId = deliveryAddressService.separateAddresses(allDeliveryAddressesListByRestaurantId);

        Page<DeliveryAddressDTO> addressesWithoutDelivery = deliveryAddressService
                .getAddressesWithoutDeliveryBasedOnPostalCode(restaurantId,
                        restaurantAddress.getPostalCode(),
                        buildPageRequestForDeliveryAddress(
                                exiAdrSortingDirection,
                                exiAdrPage
                        ));

        String EASortingDirection;
        int EAPage;
        if (addressesWithoutDelivery.getSort().isEmpty()) {
            EASortingDirection = "asc";
            EAPage = 0;
        } else {
            EASortingDirection = addressesWithoutDelivery.getSort().toString().split(" ")[1].toLowerCase();
            EAPage = addressesWithoutDelivery.getNumber();
        }

        model.addAttribute("addresses", allDeliveryAddressesByRestaurantId);
        model.addAttribute("restaurantAddress", restaurantAddress);
        model.addAttribute("addressesWithoutDelivery", addressesWithoutDelivery);
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("totalNumberOfPages", allDeliveryAddressesListByRestaurantId.getTotalPages());
        model.addAttribute("CAPage", currAdrPage);
        model.addAttribute("CASortingDirection", currAdrSortingDirection);
        model.addAttribute("EASortingDirection", EASortingDirection);
        model.addAttribute("EAPage", EAPage);
        return "delivery_addresses";
    }

    @PostMapping(REMOVE_DELIVERY_ADDRESS)
    public String removeDeliveryAddress(
            @RequestParam(name = "deliveryAddressId") Integer deliveryAddressId,
            @RequestParam(name = "restaurantId") Integer restaurantId
    ) {
        var authority = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().stream().findFirst()
                .orElseThrow(() -> new AccessDeniedException("You do not have the required authority to view this page."))
                .getAuthority();
        if (authority.equals(AvailableRoles.RESTAURANT.name())) {
            deliveryAddressService.removeDeliveryAddress(deliveryAddressId, restaurantId);

            return "redirect:" + SHOW_DELIVERY_ADDRESSES;
        }
        throw new AccessDeniedException("You do not have the required authority to view this page.");
    }

    @PostMapping(ADD_DELIVERY_ADDRESS)
    public String addDeliveryAddress(
            DeliveryAddressDTO deliveryAddressDTO,
            @RequestParam(name = "restaurantId") Integer restaurantId
    ) {
        var authority = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().stream().findFirst()
                .orElseThrow(() -> new AccessDeniedException("You do not have the required authority to view this page."))
                .getAuthority();
        if (authority.equals(AvailableRoles.RESTAURANT.name())) {
            deliveryAddressService.addDeliveryAddress(deliveryAddressDTO, restaurantId);
            return "redirect:" + SHOW_DELIVERY_ADDRESSES;
        }
        throw new AccessDeniedException("You do not have the required authority to view this page.");
    }

    private PageRequest buildPageRequestForDeliveryAddressList(String direction, Integer page) {
        if (direction.equalsIgnoreCase(PaginationAndSortingUtils.ASC.getSortingDirection())) {
            return PageRequest.of(page, 10, Sort.by("deliveryAddressEntity.streetName").ascending());
        }
        return PageRequest.of(page, 10, Sort.by("deliveryAddressEntity.streetName").descending());
    }

    private PageRequest buildPageRequestForDeliveryAddress(String direction, Integer page) {
        if (direction.equalsIgnoreCase(PaginationAndSortingUtils.ASC.getSortingDirection())) {
            return PageRequest.of(page, 10, Sort.by("streetName").ascending());
        }
        return PageRequest.of(page, 10, Sort.by("streetName").descending());
    }
}
