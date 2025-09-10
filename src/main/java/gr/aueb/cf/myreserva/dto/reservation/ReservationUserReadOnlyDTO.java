package gr.aueb.cf.myreserva.dto.reservation;

import gr.aueb.cf.myreserva.dto.user.UserReadOnlyDTO;

import java.time.LocalDateTime;

public record ReservationUserReadOnlyDTO(
        Long id,
        LocalDateTime dateTime,
        String hours,
        String text,
        UserReadOnlyDTO user
) {}