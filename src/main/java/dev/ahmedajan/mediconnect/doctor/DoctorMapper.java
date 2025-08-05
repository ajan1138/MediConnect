package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.doctor.dto.DoctorResponseDTO;
import dev.ahmedajan.mediconnect.user.User;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Builder
public class DoctorMapper {

    public DoctorProfile toDoctorProfile(User user, DoctorRegistrationRequest request){
        return DoctorProfile.builder()
                .user(user)
                .specialization(request.getSpecialization())
                .bio(request.getBio())
                .location(request.getLocation())
                .isApproved(true)
                .rate(request.getRate())
                .medicalHistory(new ArrayList<>())
                .reservedSlots(new ArrayList<>())
                .allRates(new ArrayList<>())
                .build();
    }


    public DoctorResponseDTO toDoctorResponseDTO(DoctorProfile doc) {
        return DoctorResponseDTO.builder()
                .id(doc.getId())
                .name(doc.getUser().getName())
                .fullName(doc.getUser().getFullName())
                .specialization(doc.getSpecialization())
                .location(doc.getLocation())
                .bio(doc.getBio())
                .averageRating(doc.getRate())
                .build();
    }
}
