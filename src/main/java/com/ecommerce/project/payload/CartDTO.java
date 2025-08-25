package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data model representing a user's shopping cart")
public class CartDTO {

    @Schema(description = "Unique identifier of the cart", example = "1")
    private Long cartId;

    @Schema(description = "List of products currently in the cart")
    private List<ProductDTO> products = new ArrayList<>();

    @Schema(description = "Calculated total price of all items in the cart", example = "4599.50")
    private Double totalPrice = 0.0;

}
