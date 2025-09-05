package gr.aueb.cf.myreserva.dto;

import lombok.*;

@Builder
public record AuthenticationResponseDTO (
        String firstname,
        String lastname,
        String email,
        String token
) {}
