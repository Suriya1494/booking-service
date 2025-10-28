package com.surya.Model;

import com.surya.domain.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long saloonId;

    private Long customerId;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime startTime;
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime endTime;

    @ElementCollection
    private Set<Long> serviceIds;
    private BookingStatus status=BookingStatus.PENDING;
    private int totalPrice;
}
