package com.Address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of AddressService.
 */
@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressResponseDTO create(AddressRequestDTO requestDTO) {
        log.info("Creating new address in {}, {}", requestDTO.getCity(), requestDTO.getCountry());

        Address address = new Address();
        mapRequestToEntity(requestDTO, address);
        address.setActive(true);

        Address saved = addressRepository.save(address);
        log.info("Created address with id: {}", saved.getId());

        return AddressResponseDTO.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponseDTO getById(Long id) {
        Address address = findAddressById(id);
        return AddressResponseDTO.fromEntity(address);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AddressResponseDTO> getAll(Pageable pageable) {
        return addressRepository.findAll(pageable)
                .map(AddressResponseDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AddressResponseDTO> getAllActive(Pageable pageable) {
        return addressRepository.findByActiveTrue(pageable)
                .map(AddressResponseDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AddressResponseDTO> getAllInactive(Pageable pageable) {
        return addressRepository.findByActiveFalse(pageable)
                .map(AddressResponseDTO::fromEntity);
    }

    @Override
    public AddressResponseDTO update(Long id, AddressRequestDTO requestDTO) {
        log.info("Updating address with id: {}", id);

        Address address = findAddressById(id);
        mapRequestToEntity(requestDTO, address);

        Address updated = addressRepository.save(address);
        log.info("Updated address with id: {}", updated.getId());

        return AddressResponseDTO.fromEntity(updated);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting address with id: {}", id);

        Address address = findAddressById(id);
        addressRepository.delete(address);

        log.info("Deleted address with id: {}", id);
    }

    @Override
    public void deactivate(Long id) {
        log.info("Deactivating address with id: {}", id);

        Address address = findAddressById(id);
        address.setActive(false);
        addressRepository.save(address);

        log.info("Deactivated address with id: {}", id);
    }

    @Override
    public void activate(Long id) {
        log.info("Activating address with id: {}", id);

        Address address = findAddressById(id);
        address.setActive(true);
        addressRepository.save(address);

        log.info("Activated address with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AddressResponseDTO> getByType(AddressType addressType, Pageable pageable) {
        return addressRepository.findByAddressType(addressType, pageable)
                .map(AddressResponseDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AddressResponseDTO> getByTypeActive(AddressType addressType, Pageable pageable) {
        return addressRepository.findByAddressTypeAndActiveTrue(addressType, pageable)
                .map(AddressResponseDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AddressResponseDTO> search(String city, String country, String state, String postalCode, Pageable pageable) {
        return addressRepository.searchAddresses(city, country, state, postalCode, pageable)
                .map(AddressResponseDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AddressResponseDTO> searchActive(String city, String country, Pageable pageable) {
        return addressRepository.searchActiveAddresses(city, country, pageable)
                .map(AddressResponseDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getDistinctCountries() {
        return addressRepository.findDistinctCountries();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCitiesByCountry(String country) {
        return addressRepository.findDistinctCitiesByCountry(country);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByStreetAndCityAndCountry(String street, String city, String country) {
        return addressRepository.existsByStreetAndCityAndCountry(street, city, country);
    }

    // Helper methods
    private Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(id));
    }

    private void mapRequestToEntity(AddressRequestDTO dto, Address entity) {
        if (dto.getAddressType() != null) {
            entity.setAddressType(dto.getAddressType());
        }
        entity.setStreet(dto.getStreet());
        entity.setStreet2(dto.getStreet2());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setCountry(dto.getCountry());
        entity.setPostalCode(dto.getPostalCode());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
    }
}
