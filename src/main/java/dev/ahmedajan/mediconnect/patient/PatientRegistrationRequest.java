package dev.ahmedajan.mediconnect.patient;

import dev.ahmedajan.mediconnect.auth.RegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PatientRegistrationRequest extends RegistrationRequest {
    private LocalDate dateOfBirth;

}