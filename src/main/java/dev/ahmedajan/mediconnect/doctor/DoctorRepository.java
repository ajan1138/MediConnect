package dev.ahmedajan.mediconnect.doctor;

import dev.ahmedajan.mediconnect.availabilitySlot.AvailabilitySlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.print.Doc;
import java.awt.print.Book;
import java.util.List;

public interface DoctorRepository extends JpaRepository<DoctorProfile, Long> {

    @Query("""

            """)
    List<AvailabilitySlot> getDoctorsAvailableSlots(Long id);

    @Query("""
            SELECT d
            FROM DoctorProfile d
            """)
    Page<DoctorProfile> findAllDisplayableDoctors(Pageable pageable);
}
