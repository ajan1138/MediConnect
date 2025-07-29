package dev.ahmedajan.mediconnect.availabilitySlot;

import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentRequest;
import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import org.springframework.stereotype.Service;

@Service
public class ReservedSlotMapper {

    public ReservedSlotTime toReserveSlotTime(DoctorProfile doc,  AppointmentRequest request) {

        return ReservedSlotTime.builder()
                .doctor(doc)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
    }
}
