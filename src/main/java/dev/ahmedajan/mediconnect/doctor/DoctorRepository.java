package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<DoctorProfile, Long> {

    @Query("""
            SELECT d
            FROM DoctorProfile d
            """)
    Page<DoctorProfile> findAllDisplayableDoctors(Pageable pageable);

    DoctorProfile getDoctorProfileById(long id);

    Optional<DoctorProfile> getDoctorByUser(User user);
}
