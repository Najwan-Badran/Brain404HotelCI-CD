package com.Amenity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.Hotel.Hotel;
import com.Room.Room;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "amenities")
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean isActive = true;

    @ManyToMany(mappedBy = "amenities", fetch = FetchType.LAZY)
    private Set<Hotel> hotels = new HashSet<>();

    @ManyToMany(mappedBy = "amenities", fetch = FetchType.LAZY)
    private Set<Room> rooms = new HashSet<>();

    public Amenity() {}

    public Amenity(Long id, String name, String description, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Amenity amenity)) return false;
        return id != null && id.equals(amenity.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}