package dev.ahmedajan.mediconnect.availabilitySlot;

import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public ReservedSlotTime saveReservedSlot(DoctorProfile doc, ReservedSlotTime reservedSlot) {

        LocalDateTime start = reservedSlot.getStartTime();
        LocalDateTime end = reservedSlot.getEndTime();
        LocalDate date = reservedSlot.getDate();

        List<ReservedSlotTime> reservedSlots = slotRepository.getAllByDate(date);

        for (ReservedSlotTime day : reservedSlots) {
            LocalDateTime startDay = day.getStartTime();
            LocalDateTime endDay = day.getEndTime();
            if((startDay.isAfter(start) && startDay.isBefore(end)) ||
                    (endDay.isAfter(start) && endDay.isBefore(end))) {
                throw new BusinessRuleException("There is already an appointment scheduled, try another one please");
            }
        }

        return slotRepository.save(reservedSlot);
    }

    public void deleteReservedSlot(ReservedSlotTime oldSlot) {
        slotRepository.delete(oldSlot);
    }

    public void deleteByDoctor(DoctorProfile doc) {
        slotRepository.deleteByDoctor(doc);
    }
}
