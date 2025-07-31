package dev.ahmedajan.mediconnect.auth;

import dev.ahmedajan.mediconnect.doctor.DoctorRegistrationRequest;
import dev.ahmedajan.mediconnect.doctor.DoctorService;
import dev.ahmedajan.mediconnect.patient.PatientRegistrationRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name ="Authentication")
public class AuthenticationController {

    private final AuthenticationService service;
    private final DoctorService doctorService;

    @PostMapping("/register/patient")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerPatient(@RequestBody @Valid PatientRegistrationRequest request) throws MessagingException {
        service.registerPatient(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/register/doctor")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerDoctor(@RequestBody @Valid DoctorRegistrationRequest request) throws MessagingException {
        service.registerDoctor(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/admin/approve-doctor/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveDoctor(@PathVariable Integer doctorId) throws MessagingException {
        service.approveDoctor(doctorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest req
    ) {
        return ResponseEntity.ok(service.authenticate(req));
    }

    @GetMapping("/activate-account")
    public void confirm(@RequestParam String token) throws MessagingException {
        service.activateAccount(token);
    }

}
