package pl.Aevise.SupperSpeed.business.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryAddressPageFilter {

    public static Page<DeliveryAddress> filterAddressesNotInRestaurantDeliveryList(
            List<DeliveryAddress> addressesWhereRestaurantDoesNotDeliver,
            List<DeliveryAddress> addressesWhereRestaurantDelivers,
            PageRequest pageRequest
    ) {
        List<DeliveryAddress> filteredContent = addressesWhereRestaurantDoesNotDeliver.stream()
                .filter(address -> !addressesWhereRestaurantDelivers.contains(address))
                .toList();
        return convertListToPage(filteredContent, pageRequest);
    }

    public static Page<DeliveryAddress> convertListToPage(List<DeliveryAddress> deliveryAddresses, PageRequest pageRequest) {
        return getDeliveryAddresses(deliveryAddresses, pageRequest);
    }

    private static Page<DeliveryAddress> getDeliveryAddresses(List<DeliveryAddress> deliveryAddresses, PageRequest pageRequest) {
        List<DeliveryAddress> sortedContent = getSortedContent(pageRequest, deliveryAddresses);
        List<DeliveryAddress> slicedContent = sliceSortedContent(pageRequest, sortedContent);

        if (slicedContent.isEmpty()) {
            return Page.empty();
        }
        return new PageImpl<>(slicedContent,
                pageRequest,
                sortedContent.size()
        );
    }

    private static List<DeliveryAddress> sliceSortedContent(PageRequest pageRequest, List<DeliveryAddress> sortedContent) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), sortedContent.size());

        try {
            return sortedContent.subList(start, end);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    private static List<DeliveryAddress> getSortedContent(PageRequest pageRequest, List<DeliveryAddress> content) {
        return content
                .stream()
                .sorted(SortByDeliveryAddress(pageRequest))
                .collect(Collectors.toList());
    }

    private static Comparator<DeliveryAddress> SortByDeliveryAddress(PageRequest pageRequest) {
        return (a1, a2) -> {
            for (Sort.Order order : pageRequest.getSort()) {
                int comparison = 0;
                if (order.getProperty().equals("streetName")) {
                    comparison = a1.getStreetName().compareToIgnoreCase(a2.getStreetName());
                }
                if (order.getDirection().isDescending()) {
                    comparison = -comparison;
                }
                if (comparison != 0) {
                    return comparison;
                }
            }
            return 0;
        };
    }
}
