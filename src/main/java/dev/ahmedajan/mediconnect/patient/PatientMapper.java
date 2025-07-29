package dev.ahmedajan.mediconnect.patient;

import dev.ahmedajan.mediconnect.patient.DTO.PatientResponseDTO;
import dev.ahmedajan.mediconnect.user.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PatientMapper {

    public PatientResponseDTO toPatientResponseDTO(PatientProfile patient) {
        return PatientResponseDTO.builder()
                .id(patient.getId())
                .firstName(patient.getUser().getFirstName())
                .lastName(patient.getUser().getLastName())
                .dateOfBirth(patient.getUser().getDateOfBirth())
                .email(patient.getUser().getEmail())
                .build();
    }

    public PatientProfile toPatientProfile(User user) {
        return PatientProfile.builder()
                .user(user)
                .medicalHistory(new ArrayList<>())
                .build();
    }
}
