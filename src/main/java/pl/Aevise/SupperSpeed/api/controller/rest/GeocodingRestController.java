package pl.Aevise.SupperSpeed.api.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.Aevise.SupperSpeed.business.GeocodingService;


@RestController
@AllArgsConstructor
public class GeocodingRestController {
    private final GeocodingService geocodingService;

    @GetMapping("/geocode")
    public String geocode(@RequestParam String address) {
        return geocodingService.getGeocode(address);
    }
}
