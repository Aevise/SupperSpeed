package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.Aevise.SupperSpeed.api.controller.utils.PaginationAndSortingUtils;
import pl.Aevise.SupperSpeed.api.dto.CuisineDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.CuisineMapper;
import pl.Aevise.SupperSpeed.business.dao.CuisineDAO;
import pl.Aevise.SupperSpeed.domain.Cuisine;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.CuisineEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CuisineService {

    private final CuisineDAO cuisineDAO;
    private final CuisineMapper cuisineMapper;

    public List<Cuisine> findAll() {
        List<Cuisine> cuisineList = cuisineDAO.findAll();
        log.info("Available cuisines: [{}]", cuisineList.size());
        return cuisineList;
    }


    public List<CuisineDTO> findAllAsDTO() {
        List<CuisineDTO> list = findAll().stream()
                .map(cuisineMapper::mapToDTO)
                .toList();
        if (list.isEmpty()) {
            log.warn("Did not find any cuisines");
            return List.of();
        }
        log.info("Successfully mapped List<Cuisine> to List<CuisineDTO>");
        return list;
    }

    public List<CuisineDTO> findAllSorted(String dir) {
        List<CuisineDTO> cuisines = new ArrayList<>(findAllAsDTO());

        if (!cuisines.isEmpty()) {
            if (dir.equals(PaginationAndSortingUtils.DESC.getSortingDirection())) {
                log.info("Cuisines sorted in descending direction");
                cuisines.sort(Comparator.comparing(CuisineDTO::getCuisine).reversed());
            } else {
                log.info("Cuisines sorted in ascending direction");
                cuisines.sort(Comparator.comparing(CuisineDTO::getCuisine));
            }
        }
        return cuisines;
    }

    public CuisineEntity getCuisineByName(String cuisine) {

        Optional<CuisineEntity> cuisineEntity = cuisineDAO.findByCuisineName(cuisine);

        if (cuisineEntity.isPresent()) {
            CuisineEntity currentCuisine = cuisineEntity.get();
            log.info("Successfully fetched cuisine: id:[{}], [{}]", currentCuisine.getCuisineId(), currentCuisine.getCuisine());
            return currentCuisine;
        }
        log.warn("Could not fetch cuisine named: [{}]", cuisine);
        return null;
    }
}
