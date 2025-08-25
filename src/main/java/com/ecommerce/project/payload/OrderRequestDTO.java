package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data model for placing a new order")
public class OrderRequestDTO {

    @Schema(description = "ID of the shipping address for the order", example = "101")
    private Long addressId;

    @Schema(description = "Payment method chosen by the user (e.g., 'COD', 'Card')", example = "COD")
    private String paymentMethod;

    @Schema(description = "Payment ID from the payment gateway", example = "pay_1J2k3L4m5N6o7P8q")
    private String pgPaymentId;

    @Schema(description = "Status from the payment gateway", example = "captured")
    private String pgStatus;

    @Schema(description = "Response message from the payment gateway", example = "succeeded")
    private String pgResponseMessage;

    @Schema(description = "Name of the payment gateway", example = "Stripe")
    private String pgName;

}
