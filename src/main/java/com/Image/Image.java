package com.Image;

import com.Common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Image entity for storing images associated with various entities.
 * Uses polymorphic association pattern (entityType + entityId).
 */
@Entity
@Table(name = "images", indexes = {
        @Index(name = "idx_image_entity", columnList = "entityType, entityId")
})
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "URL must not exceed 500 characters")
    @Column(nullable = false, length = 500)
    private String url;

    @Size(max = 255, message = "Alt text must not exceed 255 characters")
    @Column(name = "alt_text", length = 255)
    private String altText;

    @NotNull(message = "Entity type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 20)
    private ImageEntityType entityType;

    @NotNull(message = "Entity ID is required")
    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "is_primary", nullable = false)
    private boolean primary = false;

    @PositiveOrZero(message = "Sort order cannot be negative")
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Size(max = 100, message = "File name must not exceed 100 characters")
    @Column(name = "file_name", length = 100)
    private String fileName;

    @Size(max = 50, message = "Content type must not exceed 50 characters")
    @Column(name = "content_type", length = 50)
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    // Constructors
    public Image() {}

    public Image(String url, ImageEntityType entityType, Long entityId) {
        this.url = url;
        this.entityType = entityType;
        this.entityId = entityId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public ImageEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(ImageEntityType entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(id, image.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", entityType=" + entityType +
                ", entityId=" + entityId +
                ", primary=" + primary +
                '}';
    }
}
