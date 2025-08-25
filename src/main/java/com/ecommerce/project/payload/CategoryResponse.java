package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard response for a paginated list of categories")
public class CategoryResponse {

    @Schema(description = "List of categories on the current page")
    private List<CategoryDTO> content;

    @Schema(description = "Current page number", example = "0")
    private Integer pageNumber;

    @Schema(description = "Number of categories per page", example = "5")
    private Integer pageSize;

    @Schema(description = "Total number of categories available", example = "25")
    private Long totalElements;

    @Schema(description = "Total number of pages", example = "5")
    private Integer totalPages;

    @Schema(description = "Indicates if this is the last page", example = "false")
    private boolean lastPage;

}
