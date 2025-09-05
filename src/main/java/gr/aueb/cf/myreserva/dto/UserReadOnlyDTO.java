package gr.aueb.cf.myreserva.dto;

public record UserReadOnlyDTO (
        String username,
        String email,
        String firstname,
        String lastname,
        String phoneNumber,
        String role
) {}
