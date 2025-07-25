package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.doctor.dto.PublicDoctorDTO;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
@Builder
public class DoctorMapper {

    public DoctorProfile toDoctorProfile(){

        return null;
    }


    public static PublicDoctorDTO toDoctorDTO(DoctorProfile doc) {
        return PublicDoctorDTO.builder()
                .id(doc.getId())
                .name(doc.getUser().getName())
                .fullName(doc.getUser().getFullName())
                .specialization(doc.getSpecialization())
                .city(doc.getLocation())
                .bio(doc.getBio())
                .averageRating(doc.getRate())
                .build();
    }
}
