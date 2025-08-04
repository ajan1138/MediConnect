package dev.ahmedajan.mediconnect.prescription;

import dev.ahmedajan.mediconnect.appointment.Appointment;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;

    private String description;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] fileData;

    public Long getDoctorId() {
        return appointment.getDoctor().getId();
    }

    public Long getPatientId() {
        return appointment.getPatient().getId();
    }
}
