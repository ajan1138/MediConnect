package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.appointment.Appointment;
import dev.ahmedajan.mediconnect.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "doctors")
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    private String specialization;
    private String bio;
    private String location;
    private boolean isApproved;
    private double rate;

    @OneToMany(mappedBy = "doctor")
    List<Appointment> medicalHistory;

}
