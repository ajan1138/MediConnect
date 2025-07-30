package dev.ahmedajan.mediconnect.availabilitySlot;

import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentRequest;
import dev.ahmedajan.mediconnect.availabilitySlot.DTO.ReservedSlotTimeResponseDTO;
import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import org.springframework.stereotype.Service;

@Service
public class ReservedSlotMapper {

    public ReservedSlotTime toReserveSlotTime(DoctorProfile doc,  AppointmentRequest request) {

        return ReservedSlotTime.builder()
                .doctor(doc)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .date(request.getDate())
                .build();
    }

    public ReservedSlotTimeResponseDTO toReserveSlotTimeDTO(ReservedSlotTime request) {

        return ReservedSlotTimeResponseDTO.builder()
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
    }
}
