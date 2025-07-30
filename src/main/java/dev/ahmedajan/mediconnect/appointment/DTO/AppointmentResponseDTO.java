package dev.ahmedajan.mediconnect.appointment.DTO;

import dev.ahmedajan.mediconnect.appointment.AppointmentStatus;
import dev.ahmedajan.mediconnect.availabilitySlot.DTO.ReservedSlotTimeResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AppointmentResponseDTO {
    private String doctorName;
    private String patientName;
    private ReservedSlotTimeResponseDTO slotTime;
    private AppointmentStatus status;
    private String notes;
    private String diagnosis;
}
