package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.Aevise.SupperSpeed.business.dao.CuisineDAO;
import pl.Aevise.SupperSpeed.domain.Cuisine;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CuisineService {

    private final CuisineDAO cuisineDAO;

    public List<Cuisine> findAll() {
        List<Cuisine> cuisineList = cuisineDAO.findAll();
        log.info("Available cuisines: [{}]", cuisineList.size());
        return cuisineList;
    }

}
