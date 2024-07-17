package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.*;
import pl.Aevise.SupperSpeed.api.dto.RestRestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.domain.Restaurant;

@Mapper(componentModel = "spring",
        uses = {
                CuisineMapper.class,
                ImageMapper.class
        })
public interface RestaurantMapper {

    @Mapping(source = "image", target = "imageDTO")
    @Mapping(source = "supperUser.supperUserId", target = "userId")
    RestaurantDTO mapToDTO(final Restaurant restaurant);

    @Mapping(source = "imageDTO", target = "image")
    Restaurant mapFromDTO(RestaurantDTO restaurantDTO);

    @Mapping(source = "cuisine.cuisine", target = "cuisine")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    RestRestaurantDTO mapToRest(RestaurantDTO restaurantDTO);

    @AfterMapping
    default void setNullAddressId(@MappingTarget RestRestaurantDTO restRestaurantDTO) {
        if (restRestaurantDTO.getAddress() != null) {
            restRestaurantDTO.getAddress().setAddressId(null);
        }
    }
}
