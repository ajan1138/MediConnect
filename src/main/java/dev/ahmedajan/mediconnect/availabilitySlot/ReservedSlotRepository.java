package dev.ahmedajan.mediconnect.availabilitySlot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservedSlotRepository extends JpaRepository<ReservedSlotTime, Long> {

    @Query("""
            SELECT a
            FROM ReservedSlotTime a
            WHERE a.doctor.id = :id
            """)
    List<ReservedSlotTime> findAllNonBooked(Long id);

    List<ReservedSlotTime> getAllByDate(LocalDate date);

    @Query("SELECT COUNT(r) > 0 FROM ReservedSlotTime r WHERE r.doctor.id = :doctorId " +
            "AND r.date = :date AND r.startTime < :endTime AND r.endTime > :startTime")
    boolean existsOverlappingSlot(@Param("doctorId") Long doctorId,
                                  @Param("date") LocalDate date,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime);

}
