package gr.aueb.cf.myreserva.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ReservationInsertDTO (

        @NotNull(message = "Date is required")
        LocalDateTime dateTime,

        @NotEmpty(message = "Hours is required")
        String hours,

        @Size(max = 200, message = "Text must be at most 200 characters")
        String text,

        @NotNull(message = "UserId required")
        Long userId

) {}
