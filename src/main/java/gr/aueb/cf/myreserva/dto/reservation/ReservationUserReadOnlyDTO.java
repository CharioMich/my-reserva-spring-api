package gr.aueb.cf.myreserva.dto.reservation;

import java.time.LocalDateTime;

public record ReservationUserReadOnlyDTO(
        Long id,
        LocalDateTime dateTime,
        String hours,
        String text,
        String username,
        String email,
        String firstname,
        String lastname,
        String phoneNumber,
        String role
) {}