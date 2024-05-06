package pl.Aevise.SupperSpeed.api.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.Aevise.SupperSpeed.business.utils.ImgurPhotoService;

@RestController
@AllArgsConstructor
public class ImgurRestController {

    private final ImgurPhotoService imgurService;

    @GetMapping("photo/get/{imageHash}")
    public String getImage(@PathVariable String imageHash) {
        String image = imgurService.getImage(imageHash);

        return "/";
    }

}
