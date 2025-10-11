package com.bikeshare.api.bike;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BikeRepository extends JpaRepository<Bike, Long> {
    List<Bike> findByStatus(BikeStatus status);
    List<Bike> findByOwnerId(Long ownerId);
}