package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data model for a user's address")
public class AddressDTO {

    @Schema(description = "Unique identifier of the address", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long addressId;

    @Schema(description = "Street name and number", example = "Panská 12")
    private String street;

    @Schema(description = "Building or apartment name/number", example = "Apartment 3")
    private String buildingName;

    @Schema(description = "City", example = "Prague")
    private String city;

    @Schema(description = "State or region", example = "Prague")
    private String state;

    @Schema(description = "Country", example = "Czech republic")
    private String country;

    @Schema(description = "Postal code (ZIP code)", example = "38841")
    private String zipcode;

}
