package dev.ahmedajan.mediconnect.appointment;

import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.patient.PatientProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private DoctorProfile doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private PatientProfile patient;

    private LocalDateTime appointmentTime;

    // AppointmentStatus enum

    private String notes;

    //private Diagnosis diagnosis;


}
