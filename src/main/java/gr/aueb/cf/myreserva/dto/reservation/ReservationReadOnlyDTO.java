package gr.aueb.cf.myreserva.dto.reservation;

import java.time.LocalDate;

public record ReservationReadOnlyDTO(
        Long _id,   // We use _id only to mirror the MongoDB response that expects our front-end
        LocalDate date,
        String hours,
        String text
) {}