package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data model for payment details associated with an order")
public class PaymentDTO {

    @Schema(description = "Unique identifier of the payment record", example = "77")
    private Long paymentId;

    @Schema(description = "Payment method used for the transaction", example = "Card")
    private String paymentMethod;

    @Schema(description = "Transaction ID from the payment gateway", example = "pi_3Lq0i42eZvKYlo2C1g3a4b5d")
    private String pgPaymentId;

    @Schema(description = "Status of the payment from the gateway", example = "succeeded")
    private String pgStatus;

    @Schema(description = "Response message from the payment gateway", example = "Payment complete.")
    private String pgResponseMessage;

    @Schema(description = "Name of the payment gateway used", example = "Stripe")
    private String pgName;

}
