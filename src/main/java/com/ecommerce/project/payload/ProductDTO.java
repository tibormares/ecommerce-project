package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data model for a product")
public class ProductDTO {

    @Schema(description = "Unique identifier of the product", example = "1")
    private Long productId;

    @Schema(description = "Name of the product", example = "iPhone 15 Pro")
    private String productName;

    @Schema(description = "URL of the product image", example = "default.png")
    private String image;

    @Schema(description = "Detailed description of the product", example = "The latest Apple iPhone with Pro camera system.")
    private String description;

    @Schema(description = "Available quantity in stock", example = "100")
    private Integer quantity;

    @Schema(description = "Original price of the product", example = "29990.00")
    private double price;

    @Schema(description = "Discount percentage on the product", example = "10.0")
    private double discount;

    @Schema(description = "Calculated price after discount", example = "26991.00")
    private double specialPrice;

}
