package com.bikeshare.api.review;

import com.bikeshare.api.reservation.Reservation;
import com.bikeshare.api.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "reviews")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @ManyToOne @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}