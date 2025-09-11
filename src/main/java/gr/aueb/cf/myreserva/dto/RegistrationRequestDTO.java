package gr.aueb.cf.myreserva.dto;

import jakarta.validation.constraints.NotNull;

public record RegistrationRequestDTO(

        @NotNull
        String username,
        @NotNull
        String email,
        @NotNull
        String password,
        @NotNull
        String confirmPassword,
        @NotNull
        String firstname,
        @NotNull
        String lastname,
        @NotNull
        String phoneNumber,
        @NotNull
        String role     // In this backend user role is assigned to user/admin depending on the email (whitelist) during registration
                        // We're having the "role" here because in the myReserva app, the frontend always sends a "user" role
                        // by default so we keep it here just so it can work and actually mirror the functionality of the Node backend
) {}
