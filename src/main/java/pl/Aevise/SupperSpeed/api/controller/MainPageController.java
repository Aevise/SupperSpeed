package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.Aevise.SupperSpeed.api.controller.utils.PaginationAndSortingUtils;
import pl.Aevise.SupperSpeed.api.dto.CuisineDTO;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.CuisineService;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainPageController {

    private static final String MAIN_PAGE = "/";
    private final AddressService addressService;
    private final CuisineService cuisineService;

    @GetMapping(MAIN_PAGE)
    String getMainPage(Model model) {

        List<String> distinctCities = addressService.findDistinctCities();
        List<CuisineDTO> cuisines = cuisineService.findAllSorted(PaginationAndSortingUtils.ASC.getSortingDirection());

        model.addAttribute("distinctCities", distinctCities);
        model.addAttribute("cuisines", cuisines);
        return "main_page";
    }
}
