package com.Amenity;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepository;
    private final AmenityMapper amenityMapper;

    public AmenityServiceImpl(AmenityRepository amenityRepository,
                              AmenityMapper amenityMapper) {
        this.amenityRepository = amenityRepository;
        this.amenityMapper = amenityMapper;
    }

    @Override
    public AmenityResponseDTO create(AmenityRequestDTO requestDTO) {

        String name = normalize(requestDTO.getName());

        if (amenityRepository.existsByNameInsensitive(name)) {
            throw new AmenityAlreadyExistsException(name);
        }

        requestDTO.setName(name);

        Amenity amenity = amenityMapper.toEntity(requestDTO);
        Amenity saved = amenityRepository.save(amenity);

        return amenityMapper.toResponseDTO(saved);
    }

    @Override
    public AmenityResponseDTO getById(Long id) {

        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new AmenityNotFoundException(id));

        return amenityMapper.toResponseDTO(amenity);
    }

    @Override
    public Page<AmenityResponseDTO> getAllActive(Pageable pageable) {
        return amenityRepository.findAllByIsActiveTrue(pageable)
                .map(amenityMapper::toResponseDTO);
    }

    @Override
    public Page<AmenityResponseDTO> getAll(Pageable pageable) {
        return amenityRepository.findAll(pageable)
                .map(amenityMapper::toResponseDTO);
    }

    @Override
    public Page<AmenityResponseDTO> getAllInactive(Pageable pageable) {
        return amenityRepository.findAllByIsActiveFalse(pageable)
                .map(amenityMapper::toResponseDTO);
    }

    @Override
    public AmenityResponseDTO update(Long id, AmenityRequestDTO requestDTO) {

        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new AmenityNotFoundException(id));

        String name = normalize(requestDTO.getName());

        if (amenityRepository.existsByNameInsensitiveAndIdNot(name, id)) {
            throw new AmenityAlreadyExistsException(name);
        }

        requestDTO.setName(name);

        amenityMapper.updateEntity(amenity, requestDTO);

        return amenityMapper.toResponseDTO(amenity);
    }

    @Override
    public void softDelete(Long id) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new AmenityNotFoundException(id));

        amenity.setIsActive(false);
    }

    @Override
    public void hardDelete(Long id) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new AmenityNotFoundException(id));

        amenityRepository.delete(amenity);
    }

    @Override
    public void reactivate(Long id) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new AmenityNotFoundException(id));

        amenity.setIsActive(true);
    }

    /* =========================
       PRIVATE VALIDATION
       ========================= */
    private String normalize(String name) {

        if (name == null || name.isBlank()) {
            throw new AmenityValidationException("Amenity name cannot be empty");
        }

        return name.trim();
    }
}