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
        String role
) {}
