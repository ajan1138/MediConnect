package dev.ahmedajan.mediconnect.patient.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDTO {
    @NotBlank
    public String firstName;
    @NotBlank
    public String lastName;
    @Past
    public LocalDate dateOfBirth;
    @NotBlank
    @Email
    public String email;
//    @NotBlank
//    @Min(8)
//    @Max(30)
//    public String password;
}
