package gr.aueb.cf.myreserva.dto.reservation;

import java.time.LocalDate;

public record ReservationReadOnlyDTO(
        Long id,
        LocalDate dateTime,
        String hours,
        String text
) {}