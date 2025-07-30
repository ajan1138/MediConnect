package dev.ahmedajan.mediconnect.appointment.DTO;

import dev.ahmedajan.mediconnect.availabilitySlot.validation.ValidBusinessHours;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidBusinessHours
public class AppointmentRequest {

    @Future
    private LocalDateTime startTime;
    @Future
    private LocalDateTime endTime;
    @Future
    private LocalDate date;
    @Size(max = 256)
    private String notes;
    @Size(max = 40)
    private String diagnosis;
}
