package gr.aueb.cf.myreserva.dto.reservation;

import java.time.LocalDate;

public record ReservationPublicReadOnlyDTO(
        LocalDate date,
        String hours
) {}