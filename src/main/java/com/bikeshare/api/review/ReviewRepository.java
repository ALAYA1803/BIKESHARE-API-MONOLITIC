package com.bikeshare.api.review;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByOwnerId(Long ownerId);
    List<Review> findByReviewerId(Long reviewerId);
}