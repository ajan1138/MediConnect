package dev.ahmedajan.mediconnect.appointment;

import dev.ahmedajan.mediconnect.admin.PageResponse;
import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentRequest;
import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentResponseDTO;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotMapper;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotRepository;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotService;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotTime;
import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.doctor.DoctorRepository;
import dev.ahmedajan.mediconnect.exception.SlotNotAvailableException;
import dev.ahmedajan.mediconnect.patient.PatientProfile;
import dev.ahmedajan.mediconnect.patient.PatientRepository;
import dev.ahmedajan.mediconnect.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AppointmentService {

    private final ReservedSlotService slotService;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientRepository patientRepository;
    private final ReservedSlotMapper reservedSlotMapper;
    private final ReservedSlotRepository reservedSlotRepository;

    @Transactional
    public long reserveAppointment(Long id, AppointmentRequest request, Authentication authentication) {
        DoctorProfile doc = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Couldn't find the doctor with that id!"));

        ReservedSlotTime reservedSlot = reservedSlotMapper.toReserveSlotTime(doc, request);

        if (ifReservedSlotOverlaps(reservedSlot)) {
            throw new SlotNotAvailableException("Time slot already booked");
        }

        User user = (User) authentication.getPrincipal();
        PatientProfile patient = patientRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        if (appointmentRepository.existsByPatient_IdAndTimeSlot_Date(patient.getId(), reservedSlot.getDate())) {
            throw new IllegalStateException("Patient already has an appointment on this date");
        }

        slotService.saveReservedSlot(doc, reservedSlot);

        Appointment appointment = appointmentMapper.toAppointment(request, doc, patient, reservedSlot);

        appointmentRepository.save(appointment);

        return appointment.getId();
    }

    private boolean ifReservedSlotOverlaps(ReservedSlotTime slot) {
        return reservedSlotRepository.existsOverlappingSlot(
                slot.getDoctorId(),
                slot.getDate(),
                slot.getStartTime(),
                slot.getEndTime()
        );
    }

    public PageResponse<AppointmentResponseDTO> getUserReservations(Authentication authentication, int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<Appointment> appointments = appointmentRepository.findAllDisplayableAppointments(pageable);

        List<AppointmentResponseDTO> appointmentResponseDTOS = appointments.stream()
                .map(appointmentMapper::toAppointmentResponseDTO)
                .toList();

        return new PageResponse<>(appointmentResponseDTOS,
                appointments.getNumber(),
                appointments.getSize(),
                (int) appointments.getTotalElements(),
                appointments.getTotalPages(),
                appointments.isFirst(),
                appointments.isLast()
                );
    }
}
