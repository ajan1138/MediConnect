package dev.ahmedajan.mediconnect.rate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, Long> {

    Optional<Rate> findByDoctorIdAndPatientId(long id, long id1);
}
