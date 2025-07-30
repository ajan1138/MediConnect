package dev.ahmedajan.mediconnect.rate;

import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.patient.PatientProfile;
import dev.ahmedajan.mediconnect.rate.DTO.RateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RateMapper {

    public Rate toRate (RateRequestDTO request, DoctorProfile doc, PatientProfile patient) {
        return Rate.builder()
                .rate(request.getRate())
                .doctor(doc)
                .patient(patient)
                .build();
    }
}
