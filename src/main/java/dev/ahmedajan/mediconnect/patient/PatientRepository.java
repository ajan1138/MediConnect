package dev.ahmedajan.mediconnect.patient;

import dev.ahmedajan.mediconnect.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientProfile, Long> {


    Optional<PatientProfile> findByUser_Id(Long userId);

    Optional<PatientProfile> getPatientProfileByUser(User user);
}
