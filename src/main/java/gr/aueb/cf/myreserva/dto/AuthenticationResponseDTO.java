package gr.aueb.cf.myreserva.dto;

import gr.aueb.cf.myreserva.core.enums.Role;
import lombok.*;

@Builder
public record AuthenticationResponseDTO (
        String username,
        String email,
        String firstname,
        String lastname,
        String phoneNumber,
        String Role,
        String token
) {}
