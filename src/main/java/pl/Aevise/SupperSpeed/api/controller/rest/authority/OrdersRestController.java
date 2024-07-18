package pl.Aevise.SupperSpeed.api.controller.rest.authority;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.Aevise.SupperSpeed.api.controller.utils.URLConstants;
import pl.Aevise.SupperSpeed.api.dto.RestOrderDTO;
import pl.Aevise.SupperSpeed.business.SupperOrderService;

import java.util.List;

@RestController
@RequestMapping(URLConstants.API_AUTH_BOTH)
@AllArgsConstructor
@Tag(name = "Secured Orders Controller", description = "Endpoints for restaurant or user to view their orders")
public class OrdersRestController {

    public final static String ORDERS = "/orders";
    private final SupperOrderService supperOrderService;

    @GetMapping(ORDERS)
    @Operation(summary = "Get all orders for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of orders",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestOrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "No orders found for the user",
                    content = @Content)
    })
    public ResponseEntity<List<RestOrderDTO>> getOrders(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();

        List<RestOrderDTO> orders = supperOrderService.getRestOrdersByUserEmail(username);

        if (orders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(orders);
    }
}
