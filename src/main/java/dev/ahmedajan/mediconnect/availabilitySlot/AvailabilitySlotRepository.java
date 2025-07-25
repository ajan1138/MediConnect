package dev.ahmedajan.mediconnect.availabilitySlot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    @Query("""
            SELECT a
            FROM AvailabilitySlot a
            WHERE a.isBooked = false AND a.doctor.id = :id
            """)
    List<AvailabilitySlot> findAllNonBooked(Long id);
}
