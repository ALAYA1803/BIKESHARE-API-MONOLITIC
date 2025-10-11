package com.bikeshare.api.review;

import com.bikeshare.api.reservation.Reservation;
import com.bikeshare.api.reservation.ReservationRepository;
import com.bikeshare.api.reservation.ReservationStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Review create(CreateReviewRequest dto) {
        Reservation reservation = reservationRepository.findById(dto.reservationId())
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            throw new IllegalStateException("Solo se pueden crear reseñas para reservas completadas.");
        }

        Review review = Review.builder()
                .reservation(reservation)
                .reviewer(reservation.getRenter())
                .owner(reservation.getBike().getOwner())
                .rating(dto.rating())
                .comment(dto.comment())
                .build();

        return reviewRepository.save(review);
    }

    public List<Review> findReviewsForOwner(Long ownerId) {
        return reviewRepository.findByOwnerId(ownerId);
    }

    public List<Review> findReviewsByRenter(Long renterId) {
        return reviewRepository.findByReviewerId(renterId);
    }
}