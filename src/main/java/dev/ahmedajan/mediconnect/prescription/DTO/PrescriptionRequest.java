package dev.ahmedajan.mediconnect.prescription.DTO;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionRequest {

    @Size(max = 200)
    private String description;

    private MultipartFile file;
}
