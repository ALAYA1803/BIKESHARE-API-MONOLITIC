package com.bikeshare.api.support;

import com.bikeshare.api.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "support_tickets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SupportTicket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 160)
    private String subject;

    @Column(length = 64)
    private String category;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private SupportStatus status = SupportStatus.OPEN;

    private String attachmentUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}