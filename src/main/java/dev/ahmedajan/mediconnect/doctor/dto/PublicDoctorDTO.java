package dev.ahmedajan.mediconnect.doctor.dto;

import lombok.Builder;

@Builder
public class PublicDoctorDTO {
    private Long id;
    private String name;
    private String fullName;
    private String specialization;
    private String city;
    private String bio;
    private double averageRating;
}
