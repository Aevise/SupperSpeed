package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.ImageDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.ImageHandlingService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.business.utils.FileMigrationUtil;
import pl.Aevise.SupperSpeed.domain.Address;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.RestaurantProfileController.*;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.address1;

@WebMvcTest(controllers = RestaurantProfileController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
class RestaurantProfileControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private AddressService addressService;
    @MockBean
    private AddressMapper addressMapper;
    @MockBean
    private ImageHandlingService imageHandlingService;
    @MockBean
    private FileMigrationUtil fileMigrationUtil;

    public static Stream<Arguments> checkThatYouCanSeeHideOrShowMyRestaurantButton() {
        return Stream.of(
                Arguments.of(false, "Show my restaurant"),
                Arguments.of(true, "Hide my restaurant")
        );
    }

    @Test
    void checkThatYouCanGetRestaurantProfileWithoutUploadedLogo() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Address address = address1();
        AddressDTO addressDTO = addressDTO1();

        String expectedPartOfHTML = "<i>You can upload only png and jpeg images up to 5MB</i>";

        //when
        when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurantDTO);
        when(addressService.findById(address.getAddressId())).thenReturn(Optional.of(address));
        when(addressMapper.mapToDTO(address)).thenReturn(addressDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_PROFILE)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));
        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("restaurantDTO"))
                .andExpect(model().attribute("restaurantDTO", restaurantDTO))
                .andExpect(model().attributeExists("addressDTO"))
                .andExpect(model().attribute("addressDTO", addressDTO))
                .andExpect(model().attributeExists("restaurantId"))
                .andExpect(model().attribute("restaurantId", restaurantDTO.getRestaurantId()))
                .andExpect(view().name("restaurant_profile"));
        assertThat(content).contains(expectedPartOfHTML);
    }

    @Test
    void checkThatYouCanGetRestaurantProfileWithUploadedLogo() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        ImageDTO imageDTO = imageDTO1();
        Address address = address1();
        AddressDTO addressDTO = addressDTO1();
        String restaurantDirectory = String.format(restaurantDTO.getRestaurantId() + "_" + restaurantDTO.getRestaurantName());

        restaurantDTO.setImageDTO(imageDTO);
        String expectedPartOfHTML = String.format("src=\"/images/1_restaurant_test_1/" + imageDTO.getImageURL());

        //when
        when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurantDTO);
        when(addressService.findById(address.getAddressId())).thenReturn(Optional.of(address));
        when(addressMapper.mapToDTO(address)).thenReturn(addressDTO);
        when(imageHandlingService.getRestaurantName(restaurantDTO.getRestaurantId(), restaurantDTO.getRestaurantName())).thenReturn(restaurantDirectory);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_PROFILE)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));
        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("restaurantDTO"))
                .andExpect(model().attribute("restaurantDTO", restaurantDTO))
                .andExpect(model().attributeExists("addressDTO"))
                .andExpect(model().attribute("addressDTO", addressDTO))
                .andExpect(model().attributeExists("restaurantId"))
                .andExpect(model().attribute("restaurantId", restaurantDTO.getRestaurantId()))
                .andExpect(model().attributeExists("imageName"))
                .andExpect(model().attribute("imageName", imageDTO.getImageURL()))
                .andExpect(model().attributeExists("restaurantDirectory"))
                .andExpect(model().attribute("restaurantDirectory", restaurantDirectory))
                .andExpect(view().name("restaurant_profile"));
        assertThat(content).contains(expectedPartOfHTML);
    }

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanSeeHideOrShowMyRestaurantButton(
            Boolean restaurantHidden,
            String expectedPartOfHTML
    ) throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Address address = address1();
        AddressDTO addressDTO = addressDTO1();

        restaurantDTO.setIsShown(restaurantHidden);

        //when
        when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurantDTO);
        when(addressService.findById(address.getAddressId())).thenReturn(Optional.of(address));
        when(addressMapper.mapToDTO(address)).thenReturn(addressDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANT_PROFILE)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));
        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors());

        assertThat(content).contains(expectedPartOfHTML);
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatYouCanNotUpdateRestaurantProfileWithoutAuthority() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        AddressDTO addressDTO = addressDTO1();
        String action = "updateAddress";
        String oldName = "oldName";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = restaurantDTO.asMap();
        parametersMap.putAll(addressDTO.asMap());
        parametersMap.putIfAbsent("action", action);
        parametersMap.putIfAbsent("restaurantId", restaurantDTO.getRestaurantId().toString());
        parametersMap.putIfAbsent("oldName", oldName);
        parametersMap.forEach(params::add);
        String expectedErrorMessage = "Error:\n[You do not have the required authority to update this profile.]";

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_UPDATE)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", expectedErrorMessage))
                .andExpect(view().name("error"));
    }

    @Test
    void checkThatYouCanUpdateRestaurantAddress() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        AddressDTO addressDTO = addressDTO1();
        String action = "updateAddress";
        String oldName = "oldName";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = restaurantDTO.asMap();
        parametersMap.putAll(addressDTO.asMap());
        parametersMap.putIfAbsent("action", action);
        parametersMap.putIfAbsent("restaurantId", restaurantDTO.getRestaurantId().toString());
        parametersMap.putIfAbsent("oldName", oldName);
        parametersMap.forEach(params::add);

        //when
        doNothing().when(restaurantService).updateAddress(addressDTO, restaurantDTO.getRestaurantId());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_UPDATE)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_PROFILE));
    }

    @Test
    void checkThatYouCanUpdateRestaurantInformation() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        AddressDTO addressDTO = addressDTO1();
        String action = "updateAddress";
        String oldName = "oldName";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = restaurantDTO.asMap();
        parametersMap.putAll(addressDTO.asMap());
        parametersMap.putIfAbsent("action", action);
        parametersMap.putIfAbsent("restaurantId", restaurantDTO.getRestaurantId().toString());
        parametersMap.putIfAbsent("oldName", oldName);
        parametersMap.forEach(params::add);

        //when
        doNothing().when(restaurantService).updateRestaurantInformation(restaurantDTO, restaurantDTO.getRestaurantId());
        doNothing().when(fileMigrationUtil).migrateFilesAfterRestaurantNameChange(restaurantDTO.getRestaurantId(), oldName, restaurantDTO.getRestaurantName());

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_UPDATE)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_PROFILE));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    void checkThatYouCanNotToggleRestaurantVisibilityWithoutAuthority() throws Exception {
        //given
        Integer restaurantId = restaurantDTO1().getRestaurantId();

        String expectedErrorMessage = "Error:\n[You do not have the required authority to update this profile.]";

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_TOGGLE)
                .param("restaurantId", restaurantId.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isNotAcceptable())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", expectedErrorMessage))
                .andExpect(view().name("error"));
    }

    @Test
    void checkThatYouCanToggleRestaurantVisibility() throws Exception {
        //given
        Integer restaurantId = restaurantDTO1().getRestaurantId();

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(RESTAURANT_TOGGLE)
                .param("restaurantId", restaurantId.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + RESTAURANT_PROFILE));
    }
}