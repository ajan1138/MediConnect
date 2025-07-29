package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.auth.RegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRegistrationRequest extends RegistrationRequest {
    private String specialization;
    private String bio;
    private String location;
    //private boolean isApproved;
    private double rate;
}
