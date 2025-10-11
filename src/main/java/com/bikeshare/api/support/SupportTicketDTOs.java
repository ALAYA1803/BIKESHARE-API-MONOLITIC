package com.bikeshare.api.support;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class SupportTicketDTOs {
    public record CreateTicketRequest(
            @NotNull Long userId,
            @NotBlank String subject,
            String category,
            @NotBlank String message
    ) {}

    public record TicketResponse(
            Long id,
            Long userId,
            String subject,
            String category,
            String message,
            SupportStatus status,
            LocalDateTime createdAt
    ) {
        public static TicketResponse from(SupportTicket ticket) {
            return new TicketResponse(
                    ticket.getId(),
                    ticket.getUser().getId(),
                    ticket.getSubject(),
                    ticket.getCategory(),
                    ticket.getMessage(),
                    ticket.getStatus(),
                    ticket.getCreatedAt()
            );
        }
    }
}