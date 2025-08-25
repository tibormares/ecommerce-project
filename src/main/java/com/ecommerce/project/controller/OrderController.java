package com.ecommerce.project.controller;

import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderRequestDTO;
import com.ecommerce.project.service.OrderService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Order", description = "APIs for managing user orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

    @Operation(summary = "Place a new order", description = "Creates an order from the user's current cart. Requires user authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., cart is empty, invalid address ID)"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @PostMapping("/users/orders")
    public ResponseEntity<OrderDTO> orderProducts(@RequestBody OrderRequestDTO orderRequestDTO) {
        String emailId = authUtil.loggedInEmail();
        OrderDTO orderDTO = orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                orderRequestDTO.getPaymentMethod(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage(),
                orderRequestDTO.getPgName()
        );

        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }

}
