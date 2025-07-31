package dev.ahmedajan.mediconnect.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDTO {

    @NotEmpty(message = "Email cannot be null or empty")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not well formatted")
    private String email;

    @NotEmpty(message = "Password cannot be null or empty")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    @NotEmpty(message = "First name cannot be null or empty")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotEmpty(message = "Last name cannot be null or empty")
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(message = "Specialization cannot be blank")
    private String specialization;

    @Size(max = 1000, message = "Bio must be less than 1000 characters")
    private String bio;

    @NotBlank(message = "Location cannot be blank")
    private String location;

}
