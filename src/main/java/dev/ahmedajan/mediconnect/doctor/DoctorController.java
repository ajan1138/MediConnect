package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.admin.PageResponse;
import dev.ahmedajan.mediconnect.appointment.AppointmentStatus;
import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentResponseDTO;
import dev.ahmedajan.mediconnect.appointment.DTO.NotesDiagnosisRequest;
import dev.ahmedajan.mediconnect.doctor.dto.DoctorRequestDTO;
import dev.ahmedajan.mediconnect.doctor.dto.DoctorResponseDTO;
import dev.ahmedajan.mediconnect.patient.DTO.PatientResponseDTO;
import dev.ahmedajan.mediconnect.prescription.DTO.PrescriptionRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
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

    @PatchMapping("/appointments/{appointment-id}/complete")
    public ResponseEntity<AppointmentResponseDTO> completeAppointment(
            Authentication authentication,
            @PathVariable("appointment-id") Long appointmentId) {
        return ResponseEntity.ok(doctorService.completeAppointment(authentication, appointmentId));
    }

    @PatchMapping("/appointments/{appointment-id}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(
            Authentication authentication,
            @PathVariable("appointment-id") Long appointmentId) {
        return ResponseEntity.ok(doctorService.cancelAppointment(authentication, appointmentId));
    }

    @GetMapping("/appointments/{appointment-id}/patient")
    public ResponseEntity<PatientResponseDTO> getPatient(
            Authentication authentication, @PathVariable("appointment-id") Long appointmentId
    ) {
        return ResponseEntity.ok(doctorService.getPatient(authentication, appointmentId));
    }

    @PostMapping("/appointments/{appointment-id}")
    public ResponseEntity<Long> postNotesAndDiagnosis(
            Authentication authentication,
            @RequestBody @Valid NotesDiagnosisRequest request,
            @PathVariable("appointment-id") Long appointmentId
            ) {
        return ResponseEntity.ok(doctorService.postNotesAndDiagnosis(authentication, request, appointmentId));
    }

    @GetMapping("/appointments/upcoming")
    public ResponseEntity<PageResponse<AppointmentResponseDTO>> getUpcomingAppointmentsDoctor(
            Authentication authentication,
            @RequestParam(defaultValue = "1",required = false, name = "page") int page,
            @RequestParam(defaultValue = "10",required = false, name = "size") int size
    ) {
        return ResponseEntity.ok(doctorService.getUpcomingAppointmentsDoctor(authentication, page, size));
    }

    @GetMapping("/appointments/rejected")
    public ResponseEntity<PageResponse<AppointmentResponseDTO>> getRejectedAppointmentsDoctor(
            Authentication authentication,
            @RequestParam(defaultValue = "1",required = false, name = "page") int page,
            @RequestParam(defaultValue = "10",required = false, name = "size") int size,
            @RequestParam(required = false, name = "status") AppointmentStatus status
    ) {
        return ResponseEntity.ok(doctorService.findAppointments(authentication, page, size, status));
    }

    @PostMapping(value = "/appointments/{appointment-id}/prescription",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> postPrescription(
            Authentication authentication,
            @PathVariable("appointment-id") Long appointmentId,
            @ModelAttribute PrescriptionRequest request
            ){
        return ResponseEntity.ok(doctorService.postPrescription(authentication, appointmentId, request));
    }

    @PutMapping(value = "/prescriptions/{prescription-id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> updatePrescription(
            Authentication authentication,
            @PathVariable("prescription-id") Long prescriptionId,
            @ModelAttribute PrescriptionRequest request
    ){
        return ResponseEntity.ok(doctorService.updatePrescription(authentication, prescriptionId, request));
    }
}
