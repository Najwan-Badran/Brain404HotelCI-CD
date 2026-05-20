package com.Image;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * DTO for image creation/update requests.
 */
public class ImageRequestDTO {

    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "URL must not exceed 500 characters")
    private String url;

    @Size(max = 255, message = "Alt text must not exceed 255 characters")
    private String altText;

    @NotNull(message = "Entity type is required")
    private ImageEntityType entityType;

    @NotNull(message = "Entity ID is required")
    private Long entityId;

    private boolean primary = false;

    @PositiveOrZero(message = "Sort order cannot be negative")
    private Integer sortOrder = 0;

    // Constructors
    public ImageRequestDTO() {}

    // Getters and Setters
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
}
