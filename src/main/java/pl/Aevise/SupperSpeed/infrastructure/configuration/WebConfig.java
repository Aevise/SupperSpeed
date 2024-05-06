package pl.Aevise.SupperSpeed.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
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
}
