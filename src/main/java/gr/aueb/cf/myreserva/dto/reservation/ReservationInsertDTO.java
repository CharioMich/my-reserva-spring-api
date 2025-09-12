package gr.aueb.cf.myreserva.dto.reservation;

import gr.aueb.cf.myreserva.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ReservationInsertDTO (

        @NotNull(message = "Date is required")
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "Date must be in ISO format: yyyy-MM-dd"
        )
        LocalDate dateTime,

        @NotEmpty(message = "Hours is required")
        @Pattern(
                regexp = "^\\d{2}:(00|30)$",
                message = "Hours field must be in HH:MM format, either full hour or half an hour added (e.g. 16:00 or 16:30)"
        )
        String hours,

        @Size(max = 200, message = "Text can be at most 200 characters")
        String text

) {}
