package pl.Aevise.SupperSpeed.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.DEFAULT_IMAGE_STORAGE_FOLDER;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = new File(DEFAULT_IMAGE_STORAGE_FOLDER).getAbsolutePath();
        String resourceLocation = "file:///" + absolutePath.replace("\\", "/") + "/";

        registry.addResourceHandler("/images/**")
                .addResourceLocations(resourceLocation);
    }

    @Bean
    public WebClient webClient(ObjectMapper objectMapper) {
        final var strategies = ExchangeStrategies
                .builder()
                .codecs(configurer -> {
                    configurer
                            .defaultCodecs()
                            .jackson2JsonEncoder(
                                    new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                    configurer
                            .defaultCodecs()
                            .jackson2JsonDecoder(
                                    new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                }).build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }
}
