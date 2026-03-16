package com.Review;

import com.Booking.Booking;
import com.User.User;
import com.Room.Room;
import com.Hotel.Hotel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(
        name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "room_id"}),
                @UniqueConstraint(columnNames = {"user_id", "hotel_id"})
        }
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rating; // 1 - 5

    @Column(length = 1000)
    private String comment;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    // 🧠 Defensive validation before persist
    @PrePersist
    public void validateTarget() {
        if ((room == null && hotel == null) || (room != null && hotel != null)) {
            throw new IllegalStateException("Review must target either Room OR Hotel");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;
        return id != null && id.equals(review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}