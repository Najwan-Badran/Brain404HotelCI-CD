package com.RoomAvailability;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for RoomAvailability entity operations.
 */
@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {

    List<RoomAvailability> findByRoomIdAndDateBetween(Long roomId, LocalDate startDate, LocalDate endDate);

    Optional<RoomAvailability> findByRoomIdAndDate(Long roomId, LocalDate date);

    void deleteByRoomIdAndDateBetween(Long roomId, LocalDate startDate, LocalDate endDate);

    Page<RoomAvailability> findByRoomId(Long roomId, Pageable pageable);
}
