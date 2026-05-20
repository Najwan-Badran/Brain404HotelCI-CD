package com.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Image entity operations.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByEntityTypeAndEntityIdOrderBySortOrder(ImageEntityType entityType, Long entityId);

    Optional<Image> findByEntityTypeAndEntityIdAndPrimaryTrue(ImageEntityType entityType, Long entityId);

    void deleteByEntityTypeAndEntityId(ImageEntityType entityType, Long entityId);

    long countByEntityTypeAndEntityId(ImageEntityType entityType, Long entityId);
}
