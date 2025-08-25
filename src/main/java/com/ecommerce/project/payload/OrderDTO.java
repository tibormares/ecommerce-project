package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detailed information about a placed order")
public class OrderDTO {

    @Schema(description = "Unique identifier of the order", example = "501")
    private Long orderId;

    @Schema(description = "Email of the user who placed the order", example = "user@example.com")
    private String email;

    @Schema(description = "List of items included in the order")
    private List<OrderItemDTO> orderItems;

    @Schema(description = "Date when the order was placed", example = "2025-08-25")
    private LocalDate orderDate;

    @Schema(description = "Payment details associated with the order")
    private PaymentDTO payment;

    @Schema(description = "Total amount of the order", example = "2599.99")
    private Double totalAmount;

    @Schema(description = "Current status of the order", example = "PLACED")
    private String orderStatus;

    @Schema(description = "ID of the shipping address used for the order", example = "101")
    private Long addressId;

}
