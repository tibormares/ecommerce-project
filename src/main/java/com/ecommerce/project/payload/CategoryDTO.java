package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data model for category")
public class CategoryDTO {

    @Schema(description = "Category ID", example = "213", accessMode = Schema.AccessMode.READ_ONLY)
    private Long categoryId;

    @Schema(description = "Category name", example = "Electronics")
    private String categoryName;

}
