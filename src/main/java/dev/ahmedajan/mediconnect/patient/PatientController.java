package dev.ahmedajan.mediconnect.patient;

import dev.ahmedajan.mediconnect.admin.PageResponse;
import dev.ahmedajan.mediconnect.availabilitySlot.AvailabilitySlot;
import dev.ahmedajan.mediconnect.doctor.DoctorService;
import dev.ahmedajan.mediconnect.doctor.dto.PublicDoctorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final DoctorService doctorService;
    private final PatientService patientService;

    // doctors
    @GetMapping("/doctors")
    public ResponseEntity<PageResponse<PublicDoctorDTO>> getDoctors(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(patientService.findAllDoctors(page, size));
    }

    @GetMapping("/doctors/{doctor-id}")
    public ResponseEntity<PublicDoctorDTO> getDoctor(
            @PathVariable("doctor-id") Long id
    ) {
        return ResponseEntity.ok(patientService.findDoctorById(id));
    }

    @GetMapping("/doctors/available-slots/{doctor-id}")
    public ResponseEntity<List<AvailabilitySlot>> getDoctorsAvailableSlots(
            @PathVariable("doctor-id") Long id
    ) {
        return ResponseEntity.ok(patientService.getDoctorsAvailableSlots(id));
    }

    //

}
