package pl.Aevise.SupperSpeed.business;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
