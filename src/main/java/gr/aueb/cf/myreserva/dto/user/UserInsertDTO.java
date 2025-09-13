package gr.aueb.cf.myreserva.dto.user;

import gr.aueb.cf.myreserva.core.enums.Role;
import jakarta.validation.constraints.*;

public record UserInsertDTO(

        // TODO Implement validation

        @Email(message = "Invalid email")
        @NotEmpty
        String email,

        @NotEmpty(message = "Username name is required")
        String username,

        @NotEmpty(message = "First name is required")
        String firstname,

        @NotEmpty(message = "Last name is required")
        String lastname,

        @Pattern(
                regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\\d)(?=.*?[@#$!%&*]).{8,}$",
                message = "Invalid Password"
        )
        @NotEmpty
        String password,

        @NotEmpty
        String confirmPassword,

        @NotEmpty(message = "Phone number is required")
        @Pattern(
                regexp = "^69\\d{8}$",
                message = "Invalid Greek phone number"
        )
        String phoneNumber

//        @NotNull(message = "Role is required")
//        Role role
) {}
