package com.bikeshare.api.reservation;

import jakarta.validation.constraints.NotNull;

public record UpdateStatusDTO(
        @NotNull ReservationStatus newStatus
) {}