package gr.aueb.cf.myreserva.dto;

import gr.aueb.cf.myreserva.dto.user.UserReadOnlyDTO;
import lombok.*;

@Builder
public record AuthenticationResponseDTO (
        UserReadOnlyDTO user,
        String accessToken
) {}
