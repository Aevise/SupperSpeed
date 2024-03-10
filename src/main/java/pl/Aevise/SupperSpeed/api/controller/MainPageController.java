package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.Aevise.SupperSpeed.business.AddressService;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainPageController {

    private static final String MAIN_PAGE = "/";
    private final AddressService addressService;

    @GetMapping(MAIN_PAGE)
    String getMainPage(Model model){

        List<String> distinctCities = addressService.findDistinctCities();
        model.addAttribute("distinctCities", distinctCities);

        return "main_page";
    }
}
