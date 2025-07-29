package dev.ahmedajan.mediconnect.appointment;

import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentRequest;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotService;
import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.doctor.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppointmentService {

    private final ReservedSlotService slotService;
    private final DoctorRepository doctorRepository;

    @Transactional
    public long reserveAppointment(Long id, AppointmentRequest request) {

        DoctorProfile doc = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Couldn't find the doctor with that id!"));
        slotService.saveReservedSlot(doc, request);
        return doc.getId();
    }
}
