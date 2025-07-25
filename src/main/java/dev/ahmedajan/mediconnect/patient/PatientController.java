package dev.ahmedajan.mediconnect.patient;

import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.doctor.DoctorService;
import dev.ahmedajan.mediconnect.doctor.dto.PublicDoctorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final DoctorService doctorService;

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorProfile>> getDoctors() {
        return ResponseEntity.ok(doctorService.findAllDoctors());
    }

    @GetMapping("/doctors/{doctor-id}")
    public ResponseEntity<PublicDoctorDTO> getDoctor(
            @PathVariable("doctor-id") Long id
    ) {
        return ResponseEntity.ok(doctorService.findDoctorById(id));
    }
}
