package pl.Aevise.SupperSpeed.api.controller.utils.interfaces;

import org.springframework.data.domain.PageRequest;

public interface PageRequestUtils {
    PageRequest buildPageRequestForRatedOrders(String direction, Integer page);
    PageRequest buildPageRequestForRatedOrders(String direction, Integer page, Integer numberOfElementsInPage);
}
