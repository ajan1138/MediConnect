package dev.ahmedajan.mediconnect.availabilitySlot;

import dev.ahmedajan.mediconnect.patient.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailableSlotService {

    private final AvailabilitySlotRepository slotRepository;

    public List<AvailabilitySlot> findAllNonBooked(Long id) {
        return slotRepository.findAllNonBooked(id);
    }
}
