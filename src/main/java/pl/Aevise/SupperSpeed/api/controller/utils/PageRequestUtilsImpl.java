package pl.Aevise.SupperSpeed.api.controller.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.Aevise.SupperSpeed.api.controller.utils.interfaces.PageRequestUtils;

@Component
public class PageRequestUtilsImpl implements PageRequestUtils {

    @Override
    public PageRequest buildPageRequestForRatedOrders(String direction, Integer page) {
        if (direction.equalsIgnoreCase(PaginationAndSortingUtils.ASC.getSortingDirection())) {
            return PageRequest.of(page,
                    10,
                    Sort.by("orderId").ascending());
        }
        return PageRequest.of(page,
                10,
                Sort.by("orderId").descending());
    }

    @Override
    public PageRequest buildPageRequestForRatedOrders(String direction, Integer page, Integer numberOfElementsInPage) {
        if (direction.equalsIgnoreCase(PaginationAndSortingUtils.ASC.getSortingDirection())) {
            return PageRequest.of(page,
                    numberOfElementsInPage,
                    Sort.by("orderId").ascending());
        }
        return PageRequest.of(page,
                numberOfElementsInPage,
                Sort.by("orderId").descending());
    }
}
