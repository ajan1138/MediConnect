package dev.ahmedajan.mediconnect.appointment;

import dev.ahmedajan.mediconnect.admin.PageResponse;
import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentRequest;
import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentResponseDTO;
import dev.ahmedajan.mediconnect.appointment.DTO.NotesDiagnosisRequest;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotMapper;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotRepository;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotService;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotTime;
import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.doctor.DoctorRepository;
import dev.ahmedajan.mediconnect.exception.BusinessRuleException;
import dev.ahmedajan.mediconnect.exception.SlotNotAvailableException;
import dev.ahmedajan.mediconnect.patient.PatientLookupService;
import dev.ahmedajan.mediconnect.patient.PatientProfile;
import dev.ahmedajan.mediconnect.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static dev.ahmedajan.mediconnect.appointment.AppointmentStatus.*;

@RequiredArgsConstructor
@Service
public class AppointmentService {

    private final ReservedSlotService slotService;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final ReservedSlotMapper reservedSlotMapper;
    private final ReservedSlotRepository reservedSlotRepository;
    private final PatientLookupService patientLookupService;

    @Transactional
    public long reserveAppointment(Long id, AppointmentRequest request, Authentication authentication) {
        DoctorProfile doc = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Couldn't find the doctor with that id!"));

        ReservedSlotTime reservedSlot = reservedSlotMapper.toReserveSlotTime(doc, request);

        if (ifReservedSlotOverlaps(reservedSlot)) {
            throw new SlotNotAvailableException("Time slot already booked");
        }

        User user = (User) authentication.getPrincipal();
        PatientProfile patient = patientLookupService.getPatientByUser(user);

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

    public PageResponse<AppointmentResponseDTO> getUserAppointments(Authentication authentication, int page, int size) {

        User user = (User) authentication.getPrincipal();
        PatientProfile patient = patientLookupService.getPatientByUser(user);

        PageRequest pageable = PageRequest.of(page, size);
        Page<Appointment> appointments = appointmentRepository.findAllByPatientId(patient.getId(), pageable);

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

    public AppointmentResponseDTO getUserAppointment(PatientProfile patientProfile, Long id) {
        Appointment appointment = appointmentRepository.getAppointmentById(id)
                .orElseThrow( () -> new IllegalArgumentException("No appointment found!"));

        if (appointment.getPatient().getId() != patientProfile.getId()) {
            throw new BusinessRuleException("Cannot visit the appointment of another patient!");
        }

        return appointmentMapper.toAppointmentResponseDTO(appointment);
    }

    public void deleteAppointment(PatientProfile patient, Long id) {
        Appointment appointment = appointmentRepository.getAppointmentById(id).get();

        if (appointment.getPatient().getId() != patient.getId()) {
            throw new BusinessRuleException("Cannot delete the appointment of another patient!");
        }

        appointmentRepository.delete(appointment);
    }

    @Transactional
    public AppointmentResponseDTO updateAppointment(PatientProfile patient, Long id, AppointmentRequest request) {

        Appointment appointment = appointmentRepository.getAppointmentById(id).orElseThrow(
                () -> new IllegalArgumentException("Appointment ID not valid!")
        );

        if (patient.getId() != appointment.getPatient().getId()) {
            throw new BusinessRuleException("Cannot update appointment of another user!");
        }

        LocalDateTime now = LocalDateTime.now();
        if (appointment.getTimeSlot().getStartTime().isBefore(now.plusHours(24))) {
            throw new IllegalStateException("Cannot update appointments less than 24 hours before the scheduled time");
        }

        DoctorProfile doc = doctorRepository.getDoctorProfileById(appointment.getDoctor().getId());
        ReservedSlotTime newReservedSlot = reservedSlotMapper.toReserveSlotTime(doc, request);

        if (ifReservedSlotOverlaps(newReservedSlot)) {
            throw new SlotNotAvailableException("Time slot already booked");
        }

        if (!appointment.getTimeSlot().getDate().equals(newReservedSlot.getDate()) &&
                appointmentRepository.existsByPatient_IdAndTimeSlot_DateAndIdNot(
                        patient.getId(),
                        newReservedSlot.getDate(),
                        id)) {
            throw new IllegalStateException("Patient already has an appointment on this date");
        }

        ReservedSlotTime oldSlot = appointment.getTimeSlot();
        if (!slotsAreEqual(oldSlot, newReservedSlot)) {
            // Save new slot first to get the persisted entity
            ReservedSlotTime savedNewSlot = slotService.saveReservedSlot(doc, newReservedSlot);

            // Update appointment with the persisted slot
            appointment.setTimeSlot(savedNewSlot);

            // Remove old slot after successful update
            slotService.deleteReservedSlot(oldSlot);
        }


        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }
        if (request.getDiagnosis() != null) {
            appointment.setDiagnosis(request.getDiagnosis());
        }

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponseDTO(updatedAppointment);
    }

    private boolean slotsAreEqual(ReservedSlotTime slot1, ReservedSlotTime slot2) {
        if (slot1 == null && slot2 == null) {
            return true;
        }
        if (slot1 == null || slot2 == null) {
            return false;
        }

        return Objects.equals(slot1.getDate(), slot2.getDate()) &&
                Objects.equals(slot1.getStartTime(), slot2.getStartTime()) &&
                Objects.equals(slot1.getEndTime(), slot2.getEndTime());
    }

    public AppointmentResponseDTO acceptStatus(
            DoctorProfile doctor,
            Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("No such appointment with that id!: "
                        + appointmentId));

        if (doctor.getId() != appointment.getDoctor().getId()) {
            throw new BusinessRuleException("You can only change your appointments!");
        }

        appointment.setStatus(APPROVED);
        appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponseDTO(appointment);
    }

    public AppointmentResponseDTO declineStatus(
            DoctorProfile doctor,
            Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("No such appointment with that id!: "
                        + appointmentId));

        if (doctor.getId() != appointment.getDoctor().getId()) {
            throw new BusinessRuleException("You can only change your appointments!");
        }

        if (APPROVED.equals(appointment.getStatus()) || REJECTED.equals(appointment.getStatus())) {
            throw new BusinessRuleException("You already accepted the request!");
        }

        appointment.setStatus(AppointmentStatus.REJECTED);
        appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponseDTO(appointment);
    }

    public AppointmentResponseDTO completeAppointment(DoctorProfile doctor, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("No such appointment with that id!: "
                        + appointmentId));

        if (doctor.getId() != appointment.getDoctor().getId()) {
            throw new BusinessRuleException("You can only change your appointments!");
        }

        if (APPROVED.equals(appointment.getStatus()) || REJECTED.equals(appointment.getStatus())) {
            throw new BusinessRuleException("You already accepted the request!");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponseDTO(appointment);
    }

    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.getAppointmentById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Wrong Appointment id provided"));
    }

    public Long postNotesAndDiagnosis(Appointment appointment, @Valid NotesDiagnosisRequest request) {

        if (!Objects.equals(request.getDiagnosis(), "")) {
            appointment.setDiagnosis(request.getDiagnosis());
        }

        if (!Objects.equals(request.getNotes(), "")) {
            appointment.setNotes(request.getNotes());
        }

        return appointmentRepository.save(appointment).getId();
    }

    public AppointmentResponseDTO cancelAppointment(DoctorProfile doctor, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("No such appointment with that id!: "
                        + appointmentId));

        if (appointment.getDoctor().getId() != doctor.getId()) {
            throw new BusinessRuleException("Cannot cancel the appointment of another user!");
        }

        appointment.setStatus(CANCELLED);

        appointmentRepository.save(appointment);
        slotService.deleteReservedSlot(appointment.getTimeSlot());

        return appointmentMapper.toAppointmentResponseDTO(appointment);
    }

    public PageResponse<AppointmentResponseDTO> getUpcomingAppointmentsDoctor(long doctorId, Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.findUpcomingAppointments(doctorId, pageable);

        List<AppointmentResponseDTO> appointmentsDTO = appointments.stream()
                .map(appointmentMapper::toAppointmentResponseDTO)
                .toList();

        return new PageResponse<>(
                appointmentsDTO,
                appointments.getNumber(),
                appointments.getSize(),
                (int) appointments.getTotalElements(),
                appointments.getTotalPages(),
                appointments.isFirst(),
                appointments.isLast()
        );
    }

    public PageResponse<AppointmentResponseDTO> getRejectedAppointmentsDoctor(Long doctorId, Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.findRejectedAppointments(doctorId, pageable);

        List<AppointmentResponseDTO> appointmentsDTO = appointments.stream()
                .map(appointmentMapper::toAppointmentResponseDTO)
                .toList();

        return new PageResponse<>(
                appointmentsDTO,
                appointments.getNumber(),
                appointments.getSize(),
                (int) appointments.getTotalElements(),
                appointments.getTotalPages(),
                appointments.isFirst(),
                appointments.isLast()
        );
    }
}
