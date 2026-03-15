package com.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
        SELECT AVG(r.rating)
        FROM Review r
        WHERE r.room.id = :roomId
    """)
    Double calculateAverageForRoom(Long roomId);

    @Query("""
        SELECT AVG(r.rating)
        FROM Review r
        WHERE r.hotel.id = :hotelId
    """)
    Double calculateAverageForHotel(Long hotelId);

    List<Review> findByRoom_IdOrderByCreatedAtDesc(Long roomId);

    List<Review> findByHotel_IdOrderByCreatedAtDesc(Long hotelId);

    boolean existsByUser_IdAndRoom_Id(Long userId, Long roomId);

    boolean existsByUser_IdAndHotel_Id(Long userId, Long hotelId);

    Long countByRoom_Id(Long roomId);

    Long countByHotel_Id(Long hotelId);
}