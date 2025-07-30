package dev.ahmedajan.mediconnect.appointment;

import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentRequest;
import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentResponseDTO;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotMapper;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotTime;
import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.patient.PatientProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppointmentMapper {

    private final ReservedSlotMapper slotMapper;

    public Appointment toAppointment(AppointmentRequest request,
                                     DoctorProfile doctorProfile, PatientProfile patientProfile, ReservedSlotTime timeSlot) {
        return Appointment.builder()
                .doctor(doctorProfile)
                .patient(patientProfile)
                .timeSlot(timeSlot)
                .status(AppointmentStatus.APPROVED)
                .notes(request.getNotes())
                .diagnosis(request.getDiagnosis())
                .build();
    }

    public AppointmentResponseDTO toAppointmentResponseDTO(Appointment appointment) {
        return AppointmentResponseDTO.builder()
                .doctorName(appointment.getDoctor().getFullName())
                .patientName(appointment.getPatient().getFullName())
                .slotTime(slotMapper.toReserveSlotTimeDTO(appointment.getTimeSlot()))
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .diagnosis(appointment.getDiagnosis())
                .build();
    }

}
