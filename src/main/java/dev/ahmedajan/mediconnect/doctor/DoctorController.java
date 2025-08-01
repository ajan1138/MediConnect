package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.admin.PageResponse;
import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentResponseDTO;
import dev.ahmedajan.mediconnect.doctor.dto.DoctorRequestDTO;
import dev.ahmedajan.mediconnect.doctor.dto.DoctorResponseDTO;
import dev.ahmedajan.mediconnect.patient.DTO.PatientResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/me")
    public ResponseEntity<DoctorResponseDTO> getDoctor(Authentication authentication) {
        return ResponseEntity.ok(doctorService.getDoctor(authentication));
    }

    @PutMapping("/me")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(
            Authentication authentication,
            @RequestBody @Valid DoctorRequestDTO requestDTO
            ) {
        return ResponseEntity.ok(doctorService.updateDoctor(authentication, requestDTO));
    }

    @GetMapping("/appointments")
    public ResponseEntity<PageResponse<AppointmentResponseDTO>> getAppointments(
            Authentication authentication,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ){
        return ResponseEntity.ok(doctorService.getAppointments(authentication, page, size));
    }

    @PatchMapping("/appointments/{appointment-id}/accept")
    public ResponseEntity<AppointmentResponseDTO> acceptStatus(
            Authentication authentication,
            @PathVariable("appointment-id") Long appointmentId) {

        return ResponseEntity.ok(doctorService.acceptStatus(authentication, appointmentId));
    }

    @PatchMapping("/appointments/{appointment-id}/decline")
    public ResponseEntity<AppointmentResponseDTO> declineStatus(
            Authentication authentication,
            @PathVariable("appointment-id") Long appointmentId
            ) {

        return ResponseEntity.ok(doctorService.declineStatus(authentication, appointmentId));
    }

    @GetMapping("/appointments/{appointment-id}/patient")
    public ResponseEntity<PatientResponseDTO> getPatient(
            Authentication authentication, @PathVariable("appointment-id") Long appointmentId
    ) {
        return ResponseEntity.ok(doctorService.getPatient(authentication, appointmentId));
    }
}
