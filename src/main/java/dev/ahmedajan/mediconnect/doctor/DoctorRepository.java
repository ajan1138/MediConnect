package dev.ahmedajan.mediconnect.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<DoctorProfile, Long> {

    @Query("""
            SELECT d FROM DoctorProfile d
            """)
    List<DoctorProfile> findAllDoctors();
}
