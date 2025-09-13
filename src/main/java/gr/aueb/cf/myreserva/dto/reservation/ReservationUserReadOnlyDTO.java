package gr.aueb.cf.myreserva.dto.reservation;

import gr.aueb.cf.myreserva.dto.user.UserReadOnlyDTO;

import java.time.LocalDate;

public record ReservationUserReadOnlyDTO(
        Long _id,   // We use _id only to mirror the MongoDB response that expects our front-end
        LocalDate date,
        String hours,
        String text,
        UserReadOnlyDTO userId  // 'userId' instead of 'user', as expected from the frontend
) {}