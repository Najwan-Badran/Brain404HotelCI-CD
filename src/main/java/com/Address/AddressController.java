package com.Address;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Address operations.
 */
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddressResponseDTO> create(@Valid @RequestBody AddressRequestDTO requestDTO) {
        AddressResponseDTO created = addressService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> getById(@PathVariable Long id) {
        AddressResponseDTO address = addressService.getById(id);
        return ResponseEntity.ok(address);
    }

    @GetMapping
    public ResponseEntity<Page<AddressResponseDTO>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        Page<AddressResponseDTO> addresses = addressService.getAllActive(pageable);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AddressResponseDTO>> getAllIncludingInactive(@PageableDefault(size = 20) Pageable pageable) {
        Page<AddressResponseDTO> addresses = addressService.getAll(pageable);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AddressResponseDTO>> getInactive(@PageableDefault(size = 20) Pageable pageable) {
        Page<AddressResponseDTO> addresses = addressService.getAllInactive(pageable);
        return ResponseEntity.ok(addresses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddressResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequestDTO requestDTO) {
        AddressResponseDTO updated = addressService.update(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        addressService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        addressService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{addressType}")
    public ResponseEntity<Page<AddressResponseDTO>> getByType(
            @PathVariable AddressType addressType,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AddressResponseDTO> addresses = addressService.getByTypeActive(addressType, pageable);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/type/{addressType}/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AddressResponseDTO>> getByTypeAll(
            @PathVariable AddressType addressType,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AddressResponseDTO> addresses = addressService.getByType(addressType, pageable);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AddressResponseDTO>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AddressResponseDTO> addresses = addressService.searchActive(city, country, pageable);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/search/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AddressResponseDTO>> searchAll(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String postalCode,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AddressResponseDTO> addresses = addressService.search(city, country, state, postalCode, pageable);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/countries")
    public ResponseEntity<List<String>> getDistinctCountries() {
        List<String> countries = addressService.getDistinctCountries();
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCitiesByCountry(@RequestParam String country) {
        List<String> cities = addressService.getCitiesByCountry(country);
        return ResponseEntity.ok(cities);
    }
}
