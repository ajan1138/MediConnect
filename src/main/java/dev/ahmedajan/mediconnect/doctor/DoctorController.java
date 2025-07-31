package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.doctor.dto.DoctorRequestDTO;
import dev.ahmedajan.mediconnect.doctor.dto.DoctorResponseDTO;
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
}
