package com.bikeshare.api.support;

import com.bikeshare.api.user.User;
import com.bikeshare.api.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportTicketService {
    private final SupportTicketRepository ticketRepository;
    private final UserRepository userRepository;

    public List<SupportTicket> findTicketsByUserId(Long userId) {
        return ticketRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public SupportTicket createTicket(SupportTicketDTOs.CreateTicketRequest dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        SupportTicket ticket = SupportTicket.builder()
                .user(user)
                .subject(dto.subject())
                .category(dto.category())
                .message(dto.message())
                .status(SupportStatus.OPEN)
                .build();
        return ticketRepository.save(ticket);
    }
}