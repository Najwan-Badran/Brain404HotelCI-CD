package com.Address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for Address operations.
 */
public interface AddressService {

    AddressResponseDTO create(AddressRequestDTO requestDTO);

    AddressResponseDTO getById(Long id);

    Page<AddressResponseDTO> getAll(Pageable pageable);

    Page<AddressResponseDTO> getAllActive(Pageable pageable);

    Page<AddressResponseDTO> getAllInactive(Pageable pageable);

    AddressResponseDTO update(Long id, AddressRequestDTO requestDTO);

    void delete(Long id);

    void deactivate(Long id);

    void activate(Long id);

    // By type
    Page<AddressResponseDTO> getByType(AddressType addressType, Pageable pageable);

    Page<AddressResponseDTO> getByTypeActive(AddressType addressType, Pageable pageable);

    // Search
    Page<AddressResponseDTO> search(String city, String country, String state, String postalCode, Pageable pageable);

    Page<AddressResponseDTO> searchActive(String city, String country, Pageable pageable);

    // Utility methods
    List<String> getDistinctCountries();

    List<String> getCitiesByCountry(String country);

    boolean existsByStreetAndCityAndCountry(String street, String city, String country);
}
