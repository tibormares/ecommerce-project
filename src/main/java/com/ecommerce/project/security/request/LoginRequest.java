package com.ecommerce.project.security.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Data model for user login")
public class LoginRequest {

    @NotBlank
    @Schema(description = "Registered username of the user.", example = "newuser")
    private String username;

    @NotBlank
    @Schema(description = "User's password.", example = "userpassword123")
    private String password;

}
