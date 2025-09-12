package gr.aueb.cf.myreserva.dto.reservation;

import gr.aueb.cf.myreserva.dto.user.UserReadOnlyDTO;

import java.time.LocalDate;

public record ReservationUserReadOnlyDTO(
        Long id,
        LocalDate dateTime,
        String hours,
        String text,
        UserReadOnlyDTO user
) {}