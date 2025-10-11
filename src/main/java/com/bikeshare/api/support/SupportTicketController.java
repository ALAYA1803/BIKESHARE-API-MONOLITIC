package com.bikeshare.api.support;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/support-tickets")
@RequiredArgsConstructor
@Tag(name = "Support", description = "Gestión de tickets de soporte")
public class SupportTicketController {

    private final SupportTicketService service;

    @GetMapping
    public List<SupportTicketDTOs.TicketResponse> getTickets(@RequestParam Long userId) {
        return service.findTicketsByUserId(userId).stream()
                .map(SupportTicketDTOs.TicketResponse::from)
                .toList();
    }

    @PostMapping
    public SupportTicketDTOs.TicketResponse createTicket(@Valid @RequestBody SupportTicketDTOs.CreateTicketRequest request) {
        return SupportTicketDTOs.TicketResponse.from(service.createTicket(request));
    }
}