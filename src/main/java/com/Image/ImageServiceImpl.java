package com.Image;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Image operations.
 */
@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageServiceImpl(ImageRepository imageRepository, ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    @Override
    public ImageResponseDTO create(ImageRequestDTO dto) {
        Image image = imageMapper.toEntity(dto);

        // If this is the first image for the entity, make it primary
        long count = imageRepository.countByEntityTypeAndEntityId(dto.getEntityType(), dto.getEntityId());
        if (count == 0) {
            image.setPrimary(true);
        }

        Image saved = imageRepository.save(image);
        return imageMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ImageResponseDTO getById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));
        return imageMapper.toResponseDTO(image);
    }

    @Override
    public ImageResponseDTO update(Long id, ImageRequestDTO dto) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));

        imageMapper.updateEntity(image, dto);
        Image updated = imageRepository.save(image);
        return imageMapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));
        imageRepository.delete(image);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageResponseDTO> getByEntity(ImageEntityType entityType, Long entityId) {
        return imageRepository.findByEntityTypeAndEntityIdOrderBySortOrder(entityType, entityId)
                .stream()
                .map(imageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ImageResponseDTO setPrimary(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException(imageId));

        // Remove primary from current primary image
        imageRepository.findByEntityTypeAndEntityIdAndPrimaryTrue(image.getEntityType(), image.getEntityId())
                .ifPresent(currentPrimary -> {
                    currentPrimary.setPrimary(false);
                    imageRepository.save(currentPrimary);
                });

        // Set this image as primary
        image.setPrimary(true);
        Image updated = imageRepository.save(image);
        return imageMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteByEntity(ImageEntityType entityType, Long entityId) {
        imageRepository.deleteByEntityTypeAndEntityId(entityType, entityId);
    }
}
