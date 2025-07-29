package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.appointment.Appointment;
import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotTime;
import dev.ahmedajan.mediconnect.rate.Rate;
import dev.ahmedajan.mediconnect.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "doctors")
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    private String specialization;
    private String bio;
    private String location;
    private boolean isApproved;
    private double rate;

    @OneToMany(mappedBy = "doctor")
    List<Appointment> medicalHistory;

    @OneToMany(mappedBy = "doctor")
    List<ReservedSlotTime> reservedSlots;

    @OneToMany(mappedBy = "doctor")
    List<Rate> allRates;

}
