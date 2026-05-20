package com.Address;

/**
 * DTO for returning address information.
 */
public class AddressResponseDTO {

    private Long id;
    private AddressType addressType;
    private String street;
    private String street2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private Double latitude;
    private Double longitude;
    private String fullAddress;
    private boolean active;

    // Constructors
    public AddressResponseDTO() {}

    public AddressResponseDTO(Long id, AddressType addressType, String street, String street2,
                              String city, String state, String country, String postalCode,
                              Double latitude, Double longitude, String fullAddress, boolean active) {
        this.id = id;
        this.addressType = addressType;
        this.street = street;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fullAddress = fullAddress;
        this.active = active;
    }

    // Static factory method from entity
    public static AddressResponseDTO fromEntity(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressResponseDTO(
                address.getId(),
                address.getAddressType(),
                address.getStreet(),
                address.getStreet2(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode(),
                address.getLatitude(),
                address.getLongitude(),
                address.getFullAddress(),
                address.isActive()
        );
    }

    // Getters
    public Long getId() {
        return id;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public String getStreet() {
        return street;
    }

    public String getStreet2() {
        return street2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public boolean isActive() {
        return active;
    }
}
