package com.bikeshare.api.review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        Reviewer reviewer,
        ReviewedOwner owner
) {
    public record Reviewer(Long id, String fullName, String avatarUrl) {}
    public record ReviewedOwner(Long id, String fullName) {}

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                new Reviewer(
                        review.getReviewer().getId(),
                        review.getReviewer().getFullName(),
                        review.getReviewer().getAvatarUrl()
                ),
                new ReviewedOwner(
                        review.getOwner().getId(),
                        review.getOwner().getFullName()
                )
        );
    }
}