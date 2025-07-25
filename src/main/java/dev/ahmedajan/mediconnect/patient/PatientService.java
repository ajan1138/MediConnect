package dev.ahmedajan.mediconnect.patient;

import dev.ahmedajan.mediconnect.admin.PageResponse;
import dev.ahmedajan.mediconnect.availabilitySlot.AvailabilitySlot;
import dev.ahmedajan.mediconnect.availabilitySlot.AvailableSlotService;
import dev.ahmedajan.mediconnect.doctor.DoctorService;
import dev.ahmedajan.mediconnect.doctor.dto.PublicDoctorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final DoctorService doctorService;
    private final AvailableSlotService slotService;

    public PageResponse<PublicDoctorDTO> findAllDoctors(int page, int size){
        return doctorService.findAllDoctors(page, size);
    }

    public PublicDoctorDTO findDoctorById(Long id) {
        return doctorService.findDoctorById(id);
    }

    public List<AvailabilitySlot> getDoctorsAvailableSlots(Long id) {
        return slotService.findAllNonBooked(id);
    }
}
