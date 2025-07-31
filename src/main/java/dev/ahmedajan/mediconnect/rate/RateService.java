package dev.ahmedajan.mediconnect.rate;

import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.doctor.DoctorRepository;
import dev.ahmedajan.mediconnect.exception.BusinessRuleException;
import dev.ahmedajan.mediconnect.patient.PatientProfile;
import dev.ahmedajan.mediconnect.rate.DTO.RateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateService {

    private final DoctorRepository doctorRepository;
    private final RateMapper rateMapper;
    private final RateRepository rateRepository;

    public Long rateDoctor(PatientProfile patient, @Valid RateRequestDTO requestDTO, Long id) {

        DoctorProfile doc = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find a doctor with that id"));

        if(rateRepository.findByDoctorIdAndPatientId(doc.getId(), patient.getId()).isPresent()) {
            throw new BusinessRuleException("Cannot make 2 rates for the same doctor!");
        }

        Rate rate = rateMapper.toRate(requestDTO, doc, patient);

        return rateRepository.save(rate).getId();
    }

    public Long updateRateDoctor(PatientProfile patient, @Valid RateRequestDTO requestDTO, Long doctorId) {
        DoctorProfile doc = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find a doctor with that id"));

        Rate rate = rateRepository.findByDoctorIdAndPatientId(doc.getId(), patient.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cannot find rate!"));

        rate.setRate(requestDTO.getRate());

        return rateRepository.save(rate).getId();
    }

    public void deleteRateDoctor(PatientProfile patient, Long doctorId) {
        DoctorProfile doc = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find a doctor with that id"));

        Rate rate = rateRepository.findByDoctorIdAndPatientId(doc.getId(), patient.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cannot find rate!"));

        rateRepository.delete(rate);
    }
}
