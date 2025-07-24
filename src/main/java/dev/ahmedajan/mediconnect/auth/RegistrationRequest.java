package dev.ahmedajan.mediconnect.auth;

import dev.ahmedajan.mediconnect.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Firstname is not entered correctly")
    private String firstName;

    @NotBlank(message = "Lastname is not entered correctly")
    private String lastName;

    @NotBlank(message = "Email is not entered correctly")
    @Email(message = "Email is not well formatted")
    private String email;

    @NotBlank(message = "Password is not entered correctly")
    @Size(min = 8, message = "Password must contain at least 8 chars!")
    private String password;
    @NotNull
    private Role role;

}
