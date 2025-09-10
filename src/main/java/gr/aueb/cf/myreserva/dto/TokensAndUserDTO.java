package gr.aueb.cf.myreserva.dto;

import gr.aueb.cf.myreserva.dto.user.UserReadOnlyDTO;

public record TokensAndUserDTO(
        UserReadOnlyDTO user,
        String accessToken,
        String refreshToken
) {
}
