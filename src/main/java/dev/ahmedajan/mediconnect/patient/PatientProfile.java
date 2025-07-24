package dev.ahmedajan.mediconnect.patient;

import dev.ahmedajan.mediconnect.appointment.Appointment;
import dev.ahmedajan.mediconnect.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "patients")
public class PatientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "patient")
    List<Appointment> medicalHistory;
}
