package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.Aevise.SupperSpeed.api.controller.SearchPageController.SEARCH_PAGE;
import static pl.Aevise.SupperSpeed.util.Constants.WARSZAWA;

class SearchPageControllerIT extends AbstractITConfiguration {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void getSearchPage() {
        //given
        String url = String.format("http://localhost:%s%s%s", port, basePath, SEARCH_PAGE);
        String city = WARSZAWA;
        String streetName = "Jaskrawa1";
        String cuisine = "All";
        String currDirection = "asc";
        String currPage = "0";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("city", city);
        params.add("streetName", streetName);
        params.add("cuisine", cuisine);
        params.add("currDirection", currDirection);
        params.add("currPage", currPage);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        //when
        ResponseEntity<String> response = testRestTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, requestEntity, String.class);
        String body = response.getBody();

        //then
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(body);
        assertTrue(body.contains(city));
        assertTrue(body.contains(streetName));
        assertTrue(body.contains(cuisine));
        assertTrue(body.contains(currDirection));
        assertTrue(body.contains(currPage));
    }
}