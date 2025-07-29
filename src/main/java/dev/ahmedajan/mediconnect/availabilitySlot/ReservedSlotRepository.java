package dev.ahmedajan.mediconnect.availabilitySlot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservedSlotRepository extends JpaRepository<ReservedSlotTime, Long> {

    @Query("""
            SELECT a
            FROM ReservedSlotTime a
            WHERE a.doctor.id = :id
            """)
    List<ReservedSlotTime> findAllNonBooked(Long id);
}
