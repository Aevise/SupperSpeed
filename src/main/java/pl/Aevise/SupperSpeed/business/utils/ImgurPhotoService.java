package pl.Aevise.SupperSpeed.business.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@AllArgsConstructor
public class ImgurPhotoService {

    private final WebClient webClient;

    public String getImage(String imageHash) {
        return webClient.get()
                .uri("/image/{imageHash}", imageHash)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Consider handling this asynchronously in a real application
    }


}
