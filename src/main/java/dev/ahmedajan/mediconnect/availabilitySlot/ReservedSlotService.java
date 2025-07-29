package dev.ahmedajan.mediconnect.availabilitySlot;

import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentRequest;
import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservedSlotService {

    private final ReservedSlotRepository slotRepository;
    private final ReservedSlotMapper reservedSlotMapper;

    public List<ReservedSlotTime> findAllNonBooked(Long id) {
        return slotRepository.findAllNonBooked(id);
    }

    public void saveReservedSlot(DoctorProfile doc, AppointmentRequest request) {

        ReservedSlotTime reservedSlot = reservedSlotMapper.toReserveSlotTime(doc, request);
        LocalDateTime start = reservedSlot.getStartTime();
        LocalDateTime end = reservedSlot.getEndTime();
        LocalDate date = reservedSlot.getDate();
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        long minutes = Duration.between(start, end).toMinutes();

        if ( minutes < 15 || minutes > 60 ) {
            throw new IllegalArgumentException("Appointment must be between 15 and 60 minutes");
        }

        if (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            throw new IllegalArgumentException("Cannot schedule on Saturday and Sunday");
        }



    }
}
