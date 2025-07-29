package dev.ahmedajan.mediconnect.patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientProfile, Long> {


    Optional<PatientProfile> findByUser_Id(Long userId);

}
