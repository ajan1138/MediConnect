package dev.ahmedajan.mediconnect.appointment.DTO;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotesDiagnosisRequest {

    @Size(max = 256)
    private String notes;
    @Size(max = 40)
    private String diagnosis;
}
