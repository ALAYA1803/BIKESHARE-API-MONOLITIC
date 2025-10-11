package com.bikeshare.api.review;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Gestión de reseñas y calificaciones")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Crear una nueva reseña para una reserva completada")
    @PostMapping
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody CreateReviewRequest request) {
        Review savedReview = reviewService.create(request);
        URI location = URI.create("/api/reviews/" + savedReview.getId());
        return ResponseEntity.created(location).body(ReviewResponse.from(savedReview));
    }

    @Operation(summary = "Listar reseñas", description = "Permite listar reseñas recibidas por un propietario o hechas por un arrendatario.")
    @GetMapping
    public List<ReviewResponse> list(
            @Parameter(description = "ID del propietario para ver las reseñas que ha recibido") @RequestParam(required = false) Long ownerId,
            @Parameter(description = "ID del arrendatario para ver las reseñas que ha hecho") @RequestParam(required = false) Long renterId
    ) {
        List<Review> reviews;
        if (ownerId != null) {
            reviews = reviewService.findReviewsForOwner(ownerId);
        } else if (renterId != null) {
            reviews = reviewService.findReviewsByRenter(renterId);
        } else {
            return List.of();
        }
        return reviews.stream().map(ReviewResponse::from).toList();
    }
}