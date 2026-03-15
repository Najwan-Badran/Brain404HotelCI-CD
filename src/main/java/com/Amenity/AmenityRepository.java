package com.Amenity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    /* =========================BASIC FILTERS========================= */
    Page<Amenity> findAllByIsActiveTrue(Pageable pageable);
    Page<Amenity> findAllByIsActiveFalse(Pageable pageable);
    /* =========================HOTEL FILTERS========================= */
    Page<Amenity> findByHotels_Id(Long hotelId, Pageable pageable);
    Page<Amenity> findByHotels_IdAndIsActiveTrue(Long hotelId, Pageable pageable);
    Page<Amenity> findByHotels_IdAndIsActiveFalse(Long hotelId, Pageable pageable);
    /* =========================DUPLICATE CHECKS========================= */
    @Query("""
           SELECT COUNT(a) > 0
           FROM Amenity a
           WHERE LOWER(a.name) = LOWER(:name)
           """)
    boolean existsByNameInsensitive(@Param("name") String name);
    @Query("""
           SELECT COUNT(a) > 0
           FROM Amenity a
           WHERE LOWER(a.name) = LOWER(:name)
           AND a.id <> :id
           """)
    boolean existsByNameInsensitiveAndIdNot(@Param("name") String name,
                                            @Param("id") Long id);
}