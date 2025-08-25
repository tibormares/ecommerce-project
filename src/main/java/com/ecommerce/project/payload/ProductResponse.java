package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard response for a paginated list of products")
public class ProductResponse {

    @Schema(description = "List of products on the current page")
    private List<ProductDTO> content;

    @Schema(description = "Current page number", example = "0")
    private Integer pageNumber;

    @Schema(description = "Number of products per page", example = "10")
    private Integer pageSize;

    @Schema(description = "Total number of products available", example = "150")
    private Long totalElements;

    @Schema(description = "Total number of pages", example = "15")
    private Integer totalPages;

    @Schema(description = "Indicates if this is the last page", example = "false")
    private boolean lastPage;

}
