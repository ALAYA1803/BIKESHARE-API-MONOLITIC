package com.bikeshare.api.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationDTO(
        Long id,
        Long renterId,
        Long bikeId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal totalPrice,
        ReservationStatus status
) {
    public static ReservationDTO from(Reservation r) {
        return new ReservationDTO(
                r.getId(),
                r.getRenter().getId(),
                r.getBike().getId(),
                r.getStartDate(),
                r.getEndDate(),
                r.getTotalPrice(),
                r.getStatus()
        );
    }
}