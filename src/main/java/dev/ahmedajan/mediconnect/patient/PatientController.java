package dev.ahmedajan.mediconnect.patient;

import dev.ahmedajan.mediconnect.admin.PageResponse;
import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentRequest;
import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentResponseDTO;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotTime;
import dev.ahmedajan.mediconnect.doctor.dto.DoctorResponseDTO;
import dev.ahmedajan.mediconnect.patient.DTO.PatientRequestDTO;
import dev.ahmedajan.mediconnect.patient.DTO.PatientResponseDTO;
import dev.ahmedajan.mediconnect.rate.DTO.RateRequestDTO;
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
    public ResponseEntity<PageResponse<DoctorResponseDTO>> getDoctors(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(patientService.findAllDoctors(page, size));
    }

    @GetMapping("/doctors/{doctor-id}")
    public ResponseEntity<DoctorResponseDTO> getDoctor(
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
    public ResponseEntity<AppointmentResponseDTO> deleteAppointment(Authentication authentication,
                                                                     @PathVariable("appointment-id") Long id){
        patientService.deleteAppointment(authentication, id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/appointments/{appointment-id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            Authentication authentication,
            @PathVariable("appointment-id") Long id,
            @RequestBody AppointmentRequest request){
        return ResponseEntity.ok(patientService.updateAppointment(authentication, id, request));
    }

    @PostMapping("/doctors/{doctor-id}/rate")
    public ResponseEntity<Long> rateDoctor(Authentication authentication,
                                              @Valid @RequestBody RateRequestDTO requestDTO,
                                              @PathVariable("doctor-id") Long id){
        return ResponseEntity.ok(patientService.rateDoctor(authentication, requestDTO, id));
    }

    @PutMapping("/doctors/{doctor-id}/rate")
    public ResponseEntity<Long> updateRateDoctor(Authentication authentication,
                                           @Valid @RequestBody RateRequestDTO requestDTO,
                                           @PathVariable("doctor-id") Long id){
        return ResponseEntity.ok(patientService.updateRateDoctor(authentication, requestDTO, id));
    }

    @DeleteMapping("/doctors/{doctor-id}/rate")
    public ResponseEntity<Long> deleteRateDoctor(
            Authentication authentication,
            @PathVariable("doctor-id") Long doctorId) {
        patientService.deleteRateDoctor(authentication, doctorId);
        return ResponseEntity.noContent().build();
    }
}
