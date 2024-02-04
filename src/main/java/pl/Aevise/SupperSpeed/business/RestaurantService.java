package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.business.dao.RestaurantDAO;

@Slf4j
@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantDAO restaurantDAO;

    @Transactional
    public void deleteRestaurantById(Integer id){
        restaurantDAO.deleteRestaurantById(id);
        log.info("Deleted restaurant with id: [{}]", id);
    }
}
