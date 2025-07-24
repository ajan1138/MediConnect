package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.auth.RegistrationRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRegistrationRequest extends RegistrationRequest {
    private LocalDate dateOfBirth;

}
