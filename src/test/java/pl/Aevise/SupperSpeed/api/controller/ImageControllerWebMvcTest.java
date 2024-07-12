package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.ImageHandlingService;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.ImageController.UPLOAD_DISH_IMAGE;
import static pl.Aevise.SupperSpeed.api.controller.ImageController.UPLOAD_LOGO;
import static pl.Aevise.SupperSpeed.api.controller.RestaurantMenuEditionController.RESTAURANT_MENU_EDIT;
import static pl.Aevise.SupperSpeed.api.controller.RestaurantProfileController.RESTAURANT_PROFILE;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.dishDTO1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.restaurantDTO1;

@WebMvcTest(controllers = ImageController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
class ImageControllerWebMvcTest {

    private final MockMvc mockMvc;

    @MockBean
    private final ImageHandlingService imageHandlingService;

    @Test
    void checkThatYouCanUploadLogo() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Integer restaurantId = restaurantDTO.getRestaurantId();
        String restaurantName = restaurantDTO.getRestaurantName();
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[]{1});

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("restaurantName", restaurantName);
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.forEach(params::add);

        //when
        doNothing().when(imageHandlingService).uploadLogo(file.getBytes(), restaurantId, restaurantName);

        ResultActions result = mockMvc.perform(multipart(UPLOAD_LOGO)
                .file(file)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result.andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_PROFILE));
    }

    @Test
    void checkThatYouCanNotUploadLogoWithoutRestaurantAuthority() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Integer restaurantId = restaurantDTO.getRestaurantId();
        String restaurantName = restaurantDTO.getRestaurantName();
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[]{1});

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("restaurantName", restaurantName);
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.forEach(params::add);

        //when
        ResultActions result = mockMvc.perform(multipart(UPLOAD_LOGO)
                .file(file)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(TEST_CLIENT_EMAIL_1).authorities()));

        //then
        result.andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("error"));
    }

    @Test
    void checkThatYouCanUploadDishImage() throws Exception {
        //given
        DishDTO dishDTO = dishDTO1();
        RestaurantDTO restaurantDTO = restaurantDTO1();

        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[]{1});
        Integer dishId = dishDTO.getDishId();
        String dishName = dishDTO.getName();
        Integer restaurantId = restaurantDTO.getRestaurantId();
        String restaurantName = restaurantDTO.getRestaurantName();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("restaurantName", restaurantName);
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.putIfAbsent("dishId", dishId.toString());
        parametersMap.putIfAbsent("dishName", dishName);
        parametersMap.forEach(params::add);

        //when
        doNothing().when(imageHandlingService).uploadDishImage(file.getBytes(), dishId, dishName, restaurantName, restaurantId);

        ResultActions result = mockMvc.perform(multipart(UPLOAD_DISH_IMAGE)
                .file(file)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result.andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_MENU_EDIT));
    }

    @Test
    void checkThatYouCanNotUploadDishImageWithoutRestaurantAuthority() throws Exception {
        //given
        DishDTO dishDTO = dishDTO1();
        RestaurantDTO restaurantDTO = restaurantDTO1();

        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[]{1});
        Integer dishId = dishDTO.getDishId();
        String dishName = dishDTO.getName();
        Integer restaurantId = restaurantDTO.getRestaurantId();
        String restaurantName = restaurantDTO.getRestaurantName();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("restaurantName", restaurantName);
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.putIfAbsent("dishId", dishId.toString());
        parametersMap.putIfAbsent("dishName", dishName);
        parametersMap.forEach(params::add);

        //when
        ResultActions result = mockMvc.perform(multipart(UPLOAD_DISH_IMAGE)
                .file(file)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(TEST_CLIENT_EMAIL_1).authorities()));

        //then
        result.andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("error"));
    }
}