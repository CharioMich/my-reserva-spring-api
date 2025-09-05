package gr.aueb.cf.myreserva.dto;

import gr.aueb.cf.myreserva.core.enums.Role;
import lombok.*;

@Builder
public record AuthenticationResponseDTO (
        String firstname,
        String lastname,
        String email,
        String Role,
        String token
) {}
