package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.admin.PageResponse;
import dev.ahmedajan.mediconnect.doctor.dto.DoctorRequestDTO;
import dev.ahmedajan.mediconnect.doctor.dto.DoctorResponseDTO;
import dev.ahmedajan.mediconnect.user.User;
import dev.ahmedajan.mediconnect.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;

    public PageResponse<DoctorResponseDTO> findAllDoctors(int page, int size){
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<DoctorProfile> doctors = doctorRepository.findAllDisplayableDoctors(pageable);

        List<DoctorResponseDTO> doctorResponse = doctors.stream()
                .map(doctorMapper::toDoctorResponseDTO)
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

    public DoctorResponseDTO findDoctorById(Long id) {
        DoctorProfile doctorProfile = doctorRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Invalid ID"));

        return DoctorResponseDTO.builder()
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

    // now regarding the Doctor Controller
    public DoctorResponseDTO getDoctor(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        DoctorProfile doctor = doctorRepository.getDoctorByUser(user).orElseThrow(
                () -> new IllegalArgumentException("Please first do registration as a doctor!")
        );

        return doctorMapper.toDoctorResponseDTO(doctor);
    }

    public DoctorResponseDTO updateDoctor(Authentication authentication, DoctorRequestDTO request) {
        User user = (User) authentication.getPrincipal();
        DoctorProfile doctor = doctorRepository.getDoctorByUser(user)
                .orElseThrow( () -> new IllegalArgumentException("Couldn't find a doctor with that ID"));

        doctor.setEmail(request.getEmail());
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setBio(request.getBio());
        doctor.setLastName(request.getLastName());

        //        if (!encoder.matches(patientRequestDTO.getPassword(), user.getPassword())) {
//            user.setPassword(encoder.encode(patientRequestDTO.getPassword()));
//        }

        doctorRepository.save(doctor);
        return doctorMapper.toDoctorResponseDTO(doctor);
    }
}
