package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.business.dao.DishCategoryDAO;
import pl.Aevise.SupperSpeed.domain.DishCategory;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DishCategoryService {

    private final DishCategoryDAO dishCategoryDAO;

    @Transactional
    public List<DishCategory> findAllByRestaurant(Integer restaurantId){
        List<DishCategory> dishCategories = dishCategoryDAO.findAllByRestaurant(restaurantId);
        log.info("Found: [{}] categories", dishCategories.size());
        return dishCategories;
    }
}
