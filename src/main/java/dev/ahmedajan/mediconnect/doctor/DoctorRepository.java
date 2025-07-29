package dev.ahmedajan.mediconnect.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DoctorRepository extends JpaRepository<DoctorProfile, Long> {

    @Query("""
            SELECT d
            FROM DoctorProfile d
            """)
    Page<DoctorProfile> findAllDisplayableDoctors(Pageable pageable);
}
