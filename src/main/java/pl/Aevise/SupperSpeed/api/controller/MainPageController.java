package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.Aevise.SupperSpeed.api.controller.utils.PaginationAndSortingUtils;
import pl.Aevise.SupperSpeed.api.dto.CuisineDTO;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.CuisineService;
import pl.Aevise.SupperSpeed.infrastructure.security.SecurityService;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainPageController {

    public static final String MAIN_PAGE = "/";
    private final AddressService addressService;
    private final CuisineService cuisineService;
    private final SecurityService securityService;

    @GetMapping(MAIN_PAGE)
    String getMainPage(Model model) {

        String userRole = securityService.getUserAuthority();
        List<String> distinctCities = addressService.findDistinctCities();
        List<CuisineDTO> cuisines = cuisineService.findAllSorted(PaginationAndSortingUtils.ASC.getSortingDirection());

        model.addAttribute("distinctCities", distinctCities);
        model.addAttribute("cuisines", cuisines);
        model.addAttribute("userRole", userRole);
        return "main_page";
    }
}
