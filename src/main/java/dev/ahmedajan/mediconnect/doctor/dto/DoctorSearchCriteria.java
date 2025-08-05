package dev.ahmedajan.mediconnect.doctor.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSearchCriteria {
    @Size(max = 50, message = "Specialization name too long")
    private String specialization;

    @Size(max = 50, message = "Location name too long")
    private String location;

    @Size(max = 100, message = "Full name too long")
    private String fullName;

    @DecimalMin(value = "0.0", message = "Rating must be between 0-5")
    @DecimalMax(value = "5.0", message = "Rating must be between 0-5")
    private Double minRating;
}
