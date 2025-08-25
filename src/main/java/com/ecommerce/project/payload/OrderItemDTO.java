package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data model for an item within an order")
public class OrderItemDTO {

    @Schema(description = "Unique identifier of the order item", example = "1001")
    private Long orderItemId;

    @Schema(description = "Details of the product that was ordered")
    private ProductDTO product;

    @Schema(description = "Quantity of the product ordered", example = "2")
    private Integer quantity;

    @Schema(description = "Discount applied to the product at the time of order", example = "10.0")
    private Double discount;

    @Schema(description = "Final price of the product after discount for the ordered quantity", example = "53982.00")
    private Double orderedProductPrice;

}
