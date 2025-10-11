package com.bikeshare.api.reservation;

import com.bikeshare.api.bike.Bike;
import com.bikeshare.api.bike.BikeRepository;
import com.bikeshare.api.bike.BikeStatus;
import com.bikeshare.api.user.User;
import com.bikeshare.api.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final UserRepository userRepo;
    private final BikeRepository bikeRepo;

    /**
     * Crea una nueva reserva.
     * @param dto Datos para la creación de la reserva.
     * @return La reserva guardada.
     */
    @Transactional
    public Reservation create(CreateReservationDTO dto) {
        User renter = userRepo.findById(dto.renterId())
                .orElseThrow(() -> new IllegalArgumentException("Renter not found with id: " + dto.renterId()));
        Bike bike = bikeRepo.findById(dto.bikeId())
                .orElseThrow(() -> new IllegalArgumentException("Bike not found with id: " + dto.bikeId()));

        if (dto.startDate().isAfter(dto.endDate())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        boolean isBusy = reservationRepo.existsByBikeIdAndStatusIn(
                bike.getId(),
                List.of(ReservationStatus.PENDING, ReservationStatus.ACCEPTED)
        );

        if (isBusy) {
            throw new IllegalStateException("La bicicleta ya se encuentra reservada o con una reserva pendiente.");
        }

        long minutes = Duration.between(dto.startDate(), dto.endDate()).toMinutes();
        BigDecimal price = bike.getCostPerMinute().multiply(BigDecimal.valueOf(minutes));

        Reservation r = Reservation.builder()
                .renter(renter)
                .bike(bike)
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .totalPrice(price)
                .status(ReservationStatus.PENDING)
                .build();

        return reservationRepo.save(r);
    }

    /**
     * Obtiene una reserva por su ID.
     * @param id El ID de la reserva.
     * @return Un Optional con la reserva si se encuentra.
     */
    public Optional<Reservation> get(Long id) {
        return reservationRepo.findById(id);
    }

    /**
     * Lista las reservas, permitiendo filtrar por diferentes criterios.
     * @param renterId ID del arrendatario para filtrar.
     * @param ownerId ID del propietario de la bicicleta para filtrar.
     * @param status Estado de la reserva para filtrar.
     * @return Una lista de reservas.
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Reservation> list(Long renterId, Long ownerId, ReservationStatus status) {
        if (renterId != null) return reservationRepo.findByRenterId(renterId);
        if (ownerId != null) return reservationRepo.findByBikeOwnerId(ownerId);
        if (status != null) return reservationRepo.findByStatus(status);
        return reservationRepo.findAll();
    }

    /**
     * Actualiza el estado de una reserva.
     * @param reservationId El ID de la reserva a actualizar.
     * @param newStatus El nuevo estado.
     * @return La reserva actualizada.
     */
    @Transactional
    public Reservation updateStatus(Long reservationId, ReservationStatus newStatus) {
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + reservationId));

        // Lógica de negocio para cambiar estados de la bicicleta
        if (newStatus == ReservationStatus.ACCEPTED) {
            reservation.getBike().setStatus(BikeStatus.IN_USE);
        } else if (reservation.getStatus() == ReservationStatus.ACCEPTED &&
                (newStatus == ReservationStatus.COMPLETED || newStatus == ReservationStatus.CANCELLED)) {
            // La bicicleta vuelve a estar disponible si la reserva ACEPTADA se completa o cancela.
            reservation.getBike().setStatus(BikeStatus.AVAILABLE);
        }

        reservation.setStatus(newStatus);
        return reservationRepo.save(reservation);
    }
}