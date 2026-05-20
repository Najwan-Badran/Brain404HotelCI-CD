package com.Address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Address entity operations.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Page<Address> findByAddressType(AddressType addressType, Pageable pageable);

    Page<Address> findByActiveTrue(Pageable pageable);

    Page<Address> findByActiveFalse(Pageable pageable);

    Page<Address> findByAddressTypeAndActiveTrue(AddressType addressType, Pageable pageable);

    @Query("SELECT a FROM Address a WHERE " +
           "(:city IS NULL OR LOWER(a.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:country IS NULL OR LOWER(a.country) LIKE LOWER(CONCAT('%', :country, '%'))) AND " +
           "(:state IS NULL OR LOWER(a.state) LIKE LOWER(CONCAT('%', :state, '%'))) AND " +
           "(:postalCode IS NULL OR a.postalCode = :postalCode)")
    Page<Address> searchAddresses(
            @Param("city") String city,
            @Param("country") String country,
            @Param("state") String state,
            @Param("postalCode") String postalCode,
            Pageable pageable);

    @Query("SELECT a FROM Address a WHERE a.active = true AND " +
           "(:city IS NULL OR LOWER(a.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:country IS NULL OR LOWER(a.country) LIKE LOWER(CONCAT('%', :country, '%')))")
    Page<Address> searchActiveAddresses(
            @Param("city") String city,
            @Param("country") String country,
            Pageable pageable);

    @Query("SELECT DISTINCT a.city FROM Address a WHERE a.country = :country ORDER BY a.city")
    List<String> findDistinctCitiesByCountry(@Param("country") String country);

    @Query("SELECT DISTINCT a.country FROM Address a ORDER BY a.country")
    List<String> findDistinctCountries();

    boolean existsByStreetAndCityAndCountry(String street, String city, String country);
}
