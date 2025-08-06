package dev.ahmedajan.mediconnect.appointment;

import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


    boolean existsByPatient_IdAndTimeSlot_Date(Long patientId, LocalDate date);

    Page<Appointment> findAllByPatientId(Long patientId, Pageable pageable);

    Optional<Appointment> getAppointmentById(Long id);

    boolean existsByPatient_IdAndTimeSlot_DateAndIdNot(Long patientId, LocalDate date, Long appointmentId);

    Page<Appointment> getAppointmentByDoctor(Pageable pageable, DoctorProfile doctor);

    Appointment findByDoctor(DoctorProfile doctor);

    List<Appointment> findAllByDoctorId(long id);

    @Query(value = """

            SELECT a FROM Appointment a
    JOIN a.timeSlot r
    WHERE a.doctor.id = :doctorId
    AND (
        r.date > CURRENT_DATE OR
        (r.date = CURRENT_DATE AND r.endTime > CURRENT_TIME)
    )
    AND a.status = 'APPROVED'
    ORDER BY r.date, r.startTime
    """,
            countQuery = """
    SELECT COUNT(a) FROM Appointment a
    JOIN a.timeSlot r
    WHERE a.doctor.id = :doctorId
    AND (
        r.date > CURRENT_DATE OR
        (r.date = CURRENT_DATE AND r.endTime > CURRENT_TIME)
    )
    AND a.status = 'APPROVED'
    """)
    Page<Appointment> findUpcomingAppointments(@Param("doctorId") Long doctorId, Pageable pageable);
    }


