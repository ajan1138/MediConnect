package dev.ahmedajan.mediconnect.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {

    @NotEmpty(message = "Email name cannot be null")
    @NotBlank(message = "Email name cannot be null")
    @Email(message = "Email is not well formatted")
    private String email;

    @NotEmpty(message = "First name cannot be null")
    @NotBlank(message = "First name cannot be null")
    @Size(min = 8, message = "Password must contain at least 8 chars!")
    private String password;

}
