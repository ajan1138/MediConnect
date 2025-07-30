package dev.ahmedajan.mediconnect.appointment;

import dev.ahmedajan.mediconnect.availabilitySlot.ReservedSlotTime;
import dev.ahmedajan.mediconnect.doctor.DoctorProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByDoctorAndTimeSlot(DoctorProfile doctor, ReservedSlotTime timeSlot);

    boolean existsByPatient_IdAndTimeSlot_Date(Long patientId, LocalDate date);

    Page<Appointment> findAllByPatientId(Long patientId, Pageable pageable);
}
