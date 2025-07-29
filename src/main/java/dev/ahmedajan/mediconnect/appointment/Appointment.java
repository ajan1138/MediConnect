package dev.ahmedajan.mediconnect.appointment;

import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotTime;
import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.patient.PatientProfile;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "time_slot_id"),
        @UniqueConstraint(columnNames = "doctor_id"),
        @UniqueConstraint(columnNames = "patient_id")
})
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

    @OneToOne
    @JoinColumn(name = "reserved_slot_time", referencedColumnName = "id", unique = true)
    private ReservedSlotTime timeSlot;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    private String notes;

    private String diagnosis;
}
