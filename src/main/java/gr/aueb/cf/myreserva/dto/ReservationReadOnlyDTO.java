package gr.aueb.cf.myreserva.dto;

import java.time.LocalDateTime;

public record ReservationReadOnlyDTO(
        Long id,
        LocalDateTime dateTime,
        String hours,
        String text
) {}