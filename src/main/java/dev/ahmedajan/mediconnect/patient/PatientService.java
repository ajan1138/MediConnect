package dev.ahmedajan.mediconnect.patient;

import dev.ahmedajan.mediconnect.admin.PageResponse;
import dev.ahmedajan.mediconnect.appointment.AppointmentService;
import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentRequest;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotService;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotTime;
import dev.ahmedajan.mediconnect.doctor.DoctorService;
import dev.ahmedajan.mediconnect.doctor.dto.PublicDoctorDTO;
import dev.ahmedajan.mediconnect.patient.DTO.PatientRequestDTO;
import dev.ahmedajan.mediconnect.patient.DTO.PatientResponseDTO;
import dev.ahmedajan.mediconnect.user.TokenRepository;
import dev.ahmedajan.mediconnect.user.User;
import dev.ahmedajan.mediconnect.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final DoctorService doctorService;
    private final ReservedSlotService slotService;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AppointmentService appointmentService;
    private final TokenRepository tokenRepository;

    public PageResponse<PublicDoctorDTO> findAllDoctors(int page, int size){
        return doctorService.findAllDoctors(page, size);
    }

    public PublicDoctorDTO findDoctorById(Long id) {
        return doctorService.findDoctorById(id);
    }

    public List<ReservedSlotTime> getDoctorsAvailableSlots(Long id) {
        return slotService.findAllNonBooked(id);
    }

    //@PreAuthorize("hasAuthority('PATIENT_GET')")
    public PatientResponseDTO getPatientSettings(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long id = user.getId();
        System.out.println(id);

        PatientProfile patient = patientRepository.findByUser_Id(id)
                .orElseThrow(() ->
                        new EntityNotFoundException
                                ("Could not find a patient with same id!!"));
        return patientMapper.toPatientResponseDTO(patient);
    }

    public Long putPatientSettings(Authentication authentication, @Valid PatientRequestDTO patientRequestDTO) {
        User user = (User) authentication.getPrincipal();
        Long id = user.getId();

        PatientProfile patient = patientRepository.findByUser_Id(id)
                .orElseThrow(() ->
                        new EntityNotFoundException
                                ("Could not find a patient with same id, you hacked US!"));

        user.setFirstName(patientRequestDTO.getFirstName());
        user.setLastName(patientRequestDTO.getLastName());
        user.setDateOfBirth(patientRequestDTO.getDateOfBirth());
        user.setEmail(patientRequestDTO.getEmail());

//        if (!encoder.matches(patientRequestDTO.getPassword(), user.getPassword())) {
//            user.setPassword(encoder.encode(patientRequestDTO.getPassword()));
//        }

        userRepository.save(user);
        patient.setUser(user);
        patientRepository.save(patient);

        return patient.getId();
    }

    @Transactional
    public void deletePatient(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long id = user.getId();

        PatientProfile patient = patientRepository.findByUser_Id(id)
                .orElseThrow(() ->
                        new EntityNotFoundException
                                ("Could not find a patient with same id!"));
        tokenRepository.deleteAllByUser_Id(id);
        patientRepository.delete(patient);
        userRepository.delete(user);
    }


    public long reserveAppointment(Long id, AppointmentRequest request) {
        return appointmentService.reserveAppointment(id, request);
    }

    @Transactional
    public void createPatientProfile(User savedUser) {
        PatientProfile patient = patientMapper.toPatientProfile(savedUser);
        patientRepository.save(patient);
    }
}
