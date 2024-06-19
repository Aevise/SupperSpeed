package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.RestaurantMapper;
import pl.Aevise.SupperSpeed.business.dao.RestaurantDAO;
import pl.Aevise.SupperSpeed.domain.Image;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.RestaurantEntityMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class RestaurantService {

    private final ProfileService profileService;
    private final AddressService addressService;

    private final RestaurantDAO restaurantDAO;
    private final RestaurantEntityMapper restaurantEntityMapper;
    private final RestaurantMapper restaurantMapper;

    private final DeliveryAddressService deliveryAddressService;


    @Transactional
    public RestaurantDTO findRestaurantByEmail(String email) {

        Optional<Restaurant> byUserEmail = restaurantDAO.findByUserEmail(email);
        if (byUserEmail.isPresent()) {
            log.info("Found restaurant with email: [{}]", email);
            return restaurantMapper.mapToDTO(byUserEmail.get());
        } else {
            log.warn("User with email: [{}] not found", email);
            throw new RuntimeException("User not found");
        }
    }

    public void updateAddress(AddressDTO addressDTO, Integer restaurantId) {
        addressService.updateAddressByUserId(addressDTO, restaurantId);
        log.info("Updated address for user with id: [{}]", restaurantId);
    }

    public void updateRestaurantInformation(RestaurantDTO restaurantDTO, Integer userId) {
        Restaurant restaurant = restaurantMapper.mapFromDTO(restaurantDTO);

        restaurantDAO.updateRestaurantInformation(restaurant, userId);
        log.info("Restaurant's [{}] information updated successfully.", userId);
    }

    public Optional<Restaurant> findById(Integer restaurantId) {
        Optional<Restaurant> restaurant = restaurantDAO.findById(restaurantId);
        if (restaurant.isPresent()) {
            log.info("Found restaurant: [{}]", restaurantId);
            return restaurant;
        }
        log.warn("Couldn't find restaurant [{}]", restaurantId);
        return Optional.empty();
    }

    public List<Restaurant> findAll() {
        List<Restaurant> restaurants = restaurantDAO.findAll();
        log.info("Found [{}] restaurants", restaurants.size());
        return restaurants;
    }

    public List<RestaurantDTO> findAllByCity(String city) {
        List<Restaurant> restaurants = restaurantDAO.findAllByCity(city);

        if (!restaurants.isEmpty()) {
            log.info("Found [{}] restaurants in city [{}]", restaurants.size(), city);
            return restaurants.stream()
                    .map(restaurantMapper::mapToDTO)
                    .toList();
        }
        log.info("Could not find any restaurant in city [{}]", city);
        return List.of();
    }

    @Transactional
    public int createRestaurant(RestaurantEntity restaurantEntity) {
        /**
         * @return the id of created client. Returned id matches supper_user id.
         */
        RestaurantEntity restaurant = restaurantDAO.createRestaurant(restaurantEntity);
        log.info("Successfully created user with email: [{}]. Id:[{}]", restaurant.getSupperUser().getEmail(), restaurant.getId());
        return restaurant.getId();
    }

    public RestaurantEntity findByIdEntity(Integer restaurantId) {
        Optional<Restaurant> restaurant = findById(restaurantId);
        if (restaurant.isPresent()) {
            RestaurantEntity restaurantEntity = restaurant
                    .map(restaurantEntityMapper::mapToEntity)
                    .orElseGet(() -> {
                        log.warn("Could not map restaurant with id: [{}]", restaurantId);
                        return null;
                    });
            log.info("Successfully mapped restaurant entity with id: [{}]", restaurantId);
            return restaurantEntity;
        }
        log.warn("No restaurant to map available");
        return null;
    }

    @Transactional
    public void setLogo(Image image, Integer userId) {
        restaurantDAO.updateRestaurantLogo(userId, image);
        log.info("Restaurant's with id [{}] Logo updated successfully", userId);
    }

    public Restaurant findRestaurantById(Integer restaurantId) {
        Optional<Restaurant> restaurant = restaurantDAO.findById(restaurantId);
        if (restaurant.isPresent()) {
            log.info("Successfully found restaurant with id: [{}]", restaurantId);
            return restaurant.get();
        }
        log.warn("Could not find restaurant with id: [{}]", restaurantId);
        return null;
    }

    public RestaurantDTO findRestaurantDTOById(Integer restaurantId) {
        Optional<Restaurant> restaurant = restaurantDAO.findById(restaurantId);
        if (restaurant.isPresent()) {
            log.info("Successfully found restaurant with id: [{}]", restaurantId);
            return restaurantMapper.mapToDTO(restaurant.get());
        }
        log.warn("Could not find restaurant with id: [{}]", restaurantId);
        return null;
    }

    @Transactional
    public void toggleRestaurantVisibility(Integer userId) {
        restaurantDAO.toggleRestaurantVisibility(userId);
        log.info("Restaurant [{}] visibility changed", userId);
    }

    public void detachUserFromRestaurant(String email) {
        Restaurant restaurant = restaurantDAO.detachUserFromRestaurant(email);
        if (!restaurant.getIsShown() && restaurant.getSupperUser() == null) {
            log.info("Restaurant [{}] detached successfully", restaurant.getRestaurantId());
        } else {
            log.warn("Entity could not be detached");
        }
    }

    public Page<RestaurantDTO> findAllByCityAndStreetNameOnDelivery(String city, String streetName, PageRequest pageRequest) {
        Page<Restaurant> restaurantsDeliveringOnAddress = deliveryAddressService.getRestaurantsDeliveringOnAddress(city, streetName, pageRequest);
        if (!restaurantsDeliveringOnAddress.isEmpty()) {
            log.info("Returning [{}]/[{}], page [{}]/[{}] restaurants",
                    restaurantsDeliveringOnAddress.getNumberOfElements(),
                    restaurantsDeliveringOnAddress.getTotalElements(),
                    restaurantsDeliveringOnAddress.getNumber(),
                    restaurantsDeliveringOnAddress.getTotalPages());
            return restaurantsDeliveringOnAddress
                    .map(restaurantMapper::mapToDTO);
        }
        log.info("Restaurants do not deliver to this address: [{}]. [{}]", city, streetName);
        return Page.empty();
    }


    public Page<RestaurantDTO> findAllByCityAndStreetNameAndCuisineOnDelivery(String city, String streetName, String cuisine, PageRequest pageRequest) {
        Page<Restaurant> restaurantsDeliveringOnAddress;
        if(cuisine.equalsIgnoreCase("all")){
            log.info("Searching for all restaurants delivering to address [{}], [{}]", city, streetName);
            return findAllByCityAndStreetNameOnDelivery(city, streetName, pageRequest);
        }else {
            log.info("Searching for all restaurants with cuisine [{}], delivering to address [{}], [{}]", cuisine, city, streetName);
            restaurantsDeliveringOnAddress = deliveryAddressService.getRestaurantsDeliveringOnAddressByCuisine(city, streetName, cuisine, pageRequest);
            if(!restaurantsDeliveringOnAddress.isEmpty()){
                log.info("Found [{}] restaurants, returning [{}] elements, page [{}]/[{}]",
                        restaurantsDeliveringOnAddress.getTotalElements(),
                        restaurantsDeliveringOnAddress.getNumberOfElements(),
                        restaurantsDeliveringOnAddress.getNumber(),
                        restaurantsDeliveringOnAddress.getTotalPages());
                return restaurantsDeliveringOnAddress
                        .map(restaurantMapper::mapToDTO);
            }
            log.info("Could not find restaurants delivering to address [{}], [{}]", city, streetName);
            return Page.empty();
        }
    }

    public List<RestaurantDTO> filterRestaurantsByCuisine(String cuisine, List<Restaurant> restaurantsDeliveringOnAddress) {
        List<RestaurantDTO> filteredRestaurants = restaurantsDeliveringOnAddress.stream()
                .filter(restaurant -> restaurant.getCuisine().getCuisine().equalsIgnoreCase(cuisine))
                .map(restaurantMapper::mapToDTO)
                .toList();
        log.info("Found [{}] restaurant with cuisine [{}]",
                filteredRestaurants.size(), cuisine);
        return filteredRestaurants;
    }

    public List<RestaurantDTO> filterRestaurantDTOsByCuisine(String cuisine, List<RestaurantDTO> restaurantsDeliveringOnAddress) {
        List<RestaurantDTO> filteredRestaurants = restaurantsDeliveringOnAddress.stream()
                .filter(restaurant -> restaurant.getCuisine().getCuisine().equalsIgnoreCase(cuisine))
                .toList();
        log.info("Found [{}] restaurant with cuisine [{}]",
                filteredRestaurants.size(), cuisine);
        return filteredRestaurants;
    }

    //---------------------------------------------------------------------
    public List<String> findCuisinesByDeliveryAddress_CityAndStreetName(String city, String streetName) {
        return deliveryAddressService.getCuisineFromRestaurantsDeliveringTo(city, streetName);
    }
}
