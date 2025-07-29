package dev.ahmedajan.mediconnect.appointment;

import dev.ahmedajan.mediconnect.appointment.DTO.AppointmentRequest;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotTime;
import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.patient.PatientProfile;
import org.springframework.stereotype.Service;

@Service
public class AppointmentMapper {

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

}
