package gr.aueb.cf.myreserva.dto.user;

public record UserReadOnlyDTO (
        Long id,
        String username,
        String email,
        String firstname,
        String lastname,
        String phoneNumber,
        String role
) {}
