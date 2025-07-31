package dev.ahmedajan.mediconnect.appointment;

import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


    boolean existsByPatient_IdAndTimeSlot_Date(Long patientId, LocalDate date);

    Page<Appointment> findAllByPatientId(Long patientId, Pageable pageable);

    Optional<Appointment> getAppointmentById(Long id);

    boolean existsByPatient_IdAndTimeSlot_DateAndIdNot(Long patientId, LocalDate date, Long appointmentId);

    Page<Appointment> getAppointmentByDoctor(Pageable pageable, DoctorProfile doctor);
}

