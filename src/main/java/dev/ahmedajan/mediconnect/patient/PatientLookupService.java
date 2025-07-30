package dev.ahmedajan.mediconnect.patient;


import dev.ahmedajan.mediconnect.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientLookupService {

    private final PatientRepository patientRepository;

    public PatientProfile getPatientByUser(User user) {
        return patientRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found for user ID " + user.getId()));
    }
}
