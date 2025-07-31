package dev.ahmedajan.mediconnect.doctor.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DoctorResponseDTO {
    private Long id;
    private String name;
    private String fullName;
    private String specialization;
    private String city;
    private String bio;
    private double averageRating;
}
