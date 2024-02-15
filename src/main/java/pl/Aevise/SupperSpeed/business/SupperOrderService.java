package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.business.dao.SupperOrderDAO;
import pl.Aevise.SupperSpeed.domain.SupperOrder;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SupperOrderService {
    private final SupperOrderDAO supperOrderDAO;

    @Transactional
    public List<SupperOrder> getOrdersByRestaurantId(Integer restaurantId){
        List<SupperOrder> ordersByRestaurantId = supperOrderDAO.getOrdersByRestaurantId(restaurantId);
        log.info("Found: [{}] orders", ordersByRestaurantId.size());
        return ordersByRestaurantId;
    }

    @Transactional
    public List<SupperOrder> getOrdersByClientId(Integer clientId){
        List<SupperOrder> ordersByRestaurantId = supperOrderDAO.getOrdersByClientId(clientId);
        log.info("Found: [{}] orders", ordersByRestaurantId.size());
        return ordersByRestaurantId;
    }
}
