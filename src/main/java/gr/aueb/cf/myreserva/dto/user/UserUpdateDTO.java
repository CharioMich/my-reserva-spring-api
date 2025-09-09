package gr.aueb.cf.myreserva.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserUpdateDTO(
        @Email(message = "Invalid email")
        String email,
        String username,
        String firstname,
        String lastname,
        @Pattern(
                regexp = "^69\\d{8}$",
                message = "Invalid Greek phone number"
        )
        String phoneNumber
) {
}
