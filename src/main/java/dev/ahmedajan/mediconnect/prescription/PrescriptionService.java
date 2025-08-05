package dev.ahmedajan.mediconnect.prescription;

import dev.ahmedajan.mediconnect.appointment.Appointment;
import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.prescription.DTO.PrescriptionRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository repository;
    private final PrescriptionRepository prescriptionRepository;


    @Transactional
    public Long postPrescription(
            DoctorProfile doctor,
            Appointment appointment,
            PrescriptionRequest request) {

        MultipartFile file = request.getFile();

        if(file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        Prescription prescription;

        try {
            prescription = Prescription.builder()
                    .appointment(appointment)
                    .description(request.getDescription())
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileData(file.getBytes())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't save unfortunately!");
        }

        Prescription saved = repository.save(prescription);

        return saved.getId();
    }

    public Prescription getPrescriptionById(Long id) {
        return repository.getReferenceById(id);
    }

    public Long updatePrescription(
            DoctorProfile doctor,
            Long prescriptionId,
            PrescriptionRequest request) {

        MultipartFile file = request.getFile();

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Illegal id for prescription!"));

        try {
        prescription.setFileData(request.getFile().getBytes());
        prescription.setDescription(request.getDescription());
        } catch (IOException e) {
            throw new RuntimeException("Couldn't save unfortunately!");
        }

        return prescriptionRepository.save(prescription).getId();
    }
}
