package dev.ahmedajan.mediconnect.patient;

import dev.ahmedajan.mediconnect.admin.PageResponse;
import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentRequest;
import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentResponseDTO;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotTime;
import dev.ahmedajan.mediconnect.doctor.dto.PublicDoctorDTO;
import dev.ahmedajan.mediconnect.patient.DTO.PatientRequestDTO;
import dev.ahmedajan.mediconnect.patient.DTO.PatientResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    // doctors
    @GetMapping("/doctors")
    public ResponseEntity<PageResponse<PublicDoctorDTO>> getDoctors(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(patientService.findAllDoctors(page, size));
    }
    patientService
    @GetMapping("/doctors/{doctor-id}")
    public ResponseEntity<PublicDoctorDTO> getDoctor(
            @PathVariable("doctor-id") Long id
    ) {
        return ResponseEntity.ok(patientService.findDoctorById(id));
    }

    @GetMapping("/doctors/available-slots/{doctor-id}")
    public ResponseEntity<List<ReservedSlotTime>> getDoctorsAvailableSlots(
            @PathVariable("doctor-id") Long id
    ) {
        //refactor
        return ResponseEntity.ok(patientService.getDoctorsAvailableSlots(id));
    }

    // settings get and update

    @GetMapping("/settings")
    public ResponseEntity<PatientResponseDTO> getPatientSettings(
            Authentication authentication
    ) {
        PatientResponseDTO dto = patientService.getPatientSettings(authentication);
        System.out.println("Returning patient DTO: " + dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/settings")
    public ResponseEntity<Long> updatePatientSettings(
            @RequestBody @Valid PatientRequestDTO patientRequestDTO,
            Authentication authentication
    ) {
        return ResponseEntity.ok(patientService.putPatientSettings(authentication, patientRequestDTO));
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<PatientProfile> deletePatient(Authentication authentication) {
        patientService.deletePatient(authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/doctors/{doctor-id}/reserve")
    public long reserveAppointment(
            @PathVariable("doctor-id") Long id,
            @RequestBody @Valid AppointmentRequest request,
            Authentication authentication
            ) {
        return patientService.reserveAppointment(id, request, authentication);
    }

    @GetMapping("/appointments")
    public ResponseEntity<PageResponse<AppointmentResponseDTO>> getUserAppointments(
            Authentication authentication,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size){
        return ResponseEntity.ok(patientService.getUserAppointments(authentication, page, size));
    }

    @GetMapping("/appointments/{appointment-id}")
    public ResponseEntity<AppointmentResponseDTO> getUserAppointments(Authentication authentication,
                                                                     @PathVariable("appointment-id") Long id){
        return ResponseEntity.ok(patientService.getUserAppointment(authentication, id));
    }

    @DeleteMapping("/appointments/{appointment-id}")
    public ResponseEntity<AppointmentResponseDTO> deleteAppointments(Authentication authentication,
                                                                     @PathVariable("appointment-id") Long id){
        patientService.deleteAppointment(authentication, id);
        return ResponseEntity.noContent().build();
    }

}
