package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.admin.PageResponse;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotRepository;
import dev.ahmedajan.mediconnect.doctor.dto.PublicDoctorDTO;
import dev.ahmedajan.mediconnect.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ReservedSlotRepository slotRepository;
    private final DoctorMapper doctorMapper;

    public PageResponse<PublicDoctorDTO> findAllDoctors(int page, int size){
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<DoctorProfile> doctors = doctorRepository.findAllDisplayableDoctors(pageable);

        List<PublicDoctorDTO> doctorResponse = doctors.stream()
                .map(DoctorMapper::toDoctorDTO)
                .toList();

        return new PageResponse<>(
                doctorResponse,
                doctors.getNumber(),
                doctors.getSize(),
                (int) doctors.getTotalElements(),
                doctors.getTotalPages(),
                doctors.isFirst(),
                doctors.isLast()
        );
    }

    public PublicDoctorDTO findDoctorById(Long id) {
        DoctorProfile doctorProfile = doctorRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Invalid ID"));

        return PublicDoctorDTO.builder()
                .id(doctorProfile.getId())
                .fullName(doctorProfile.getUser().getFirstName() + " " + doctorProfile.getUser().getLastName())
                .specialization(doctorProfile.getSpecialization())
                .city(doctorProfile.getLocation())
                .bio(doctorProfile.getBio())
                .averageRating(doctorProfile.getRate())
                .build();
    }

    public void createDoctorProfile(User savedUser, DoctorRegistrationRequest request) {
        System.out.println(request);
        DoctorProfile doctor = doctorMapper.toDoctorProfile(savedUser, request);
        doctorRepository.save(doctor);
    }

    // will update this to be per day

}
