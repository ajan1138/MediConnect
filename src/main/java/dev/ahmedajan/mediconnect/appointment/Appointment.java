package dev.ahmedajan.mediconnect.appointment;

import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotTime;
import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import dev.ahmedajan.mediconnect.patient.PatientProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "time_slot_id"),
})
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private DoctorProfile doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private PatientProfile patient;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "reserved_slot_time", referencedColumnName = "id", unique = true)
    private ReservedSlotTime timeSlot;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    private String notes;

    private String diagnosis;

    public LocalDate getDate() {
        return timeSlot.getDate();
    }
}
