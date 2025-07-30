package dev.ahmedajan.mediconnect.availabilitySlot.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ReservedSlotTimeResponseDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
