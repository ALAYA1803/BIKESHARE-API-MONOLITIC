package com.bikeshare.api.bike;

import com.bikeshare.api.user.User;
import com.bikeshare.api.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BikeService {
    private final BikeRepository bikes;
    private final UserRepository users;

    public BikeService(BikeRepository bikes, UserRepository users) {
        this.bikes = bikes;
        this.users = users;
    }

    @Transactional(readOnly = true)
    public List<Bike> list(BikeStatus status, Long ownerId) {
        if (status != null) return bikes.findByStatus(status);
        if (ownerId != null) return bikes.findByOwnerId(ownerId);
        return bikes.findAll();
    }

    @Transactional
    public Bike create(BikeDTOs.Create in) {
        User owner = users.findById(in.ownerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        Bike b = Bike.builder()
                .owner(owner)
                .model(in.model())
                .type(in.type())
                .costPerMinute(in.costPerMinute())
                .imageUrl(in.imageUrl())
                .latitude(in.latitude())
                .longitude(in.longitude())
                .status(BikeStatus.AVAILABLE)
                .build();
        return bikes.save(b);
    }

    @Transactional(readOnly = true)
    public Bike get(Long id) {
        return bikes.findById(id).orElseThrow(() -> new IllegalArgumentException("Bike not found"));
    }

    @Transactional
    public Bike update(Long id, BikeDTOs.Update in) {
        Bike b = get(id);
        if (in.model() != null) b.setModel(in.model());
        if (in.type() != null) b.setType(in.type());
        if (in.costPerMinute() != null) b.setCostPerMinute(in.costPerMinute());
        if (in.imageUrl() != null) b.setImageUrl(in.imageUrl());
        if (in.latitude() != null) b.setLatitude(in.latitude());
        if (in.longitude() != null) b.setLongitude(in.longitude());
        if (in.status() != null) b.setStatus(in.status());
        return bikes.save(b);
    }

    @Transactional
    public void delete(Long id) {
        bikes.deleteById(id);
    }
}