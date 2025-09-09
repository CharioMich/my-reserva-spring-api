package gr.aueb.cf.myreserva.dto.reservation;

import java.time.LocalDateTime;

public record ReservationReadOnlyDTO(
        Long id,
        LocalDateTime dateTime,
        String hours,
        String text
) {}