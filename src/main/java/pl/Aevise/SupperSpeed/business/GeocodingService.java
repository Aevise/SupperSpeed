package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodingService {

//    @Value("${google.api.key}")
//    private String apiKey;
//
//    @Value("${google.geocode.url}")
//    private String googleGeocodeUrl;

    private final RestTemplate restTemplate;

    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getGeocode(String address) {
//        String urlTemplate = UriComponentsBuilder.fromHttpUrl(googleGeocodeUrl)
//                .queryParam("address", address)
//                .queryParam("key", apiKey)
//                .toUriString();
//
//        return restTemplate.getForObject(urlTemplate, String.class);
        return "";
    }
}
