package com.ecommerce.project.security.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Data model for new user registration")
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    @Schema(description = "Desired username for the new account.", example = "newuser")
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    @Schema(description = "User's email address.", example = "user@example.com")
    private String email;

    @NotBlank
    @Size(min = 4, max = 16)
    @Schema(description = "Desired password for the new account.", example = "userpassword123")
    private String password;

}
