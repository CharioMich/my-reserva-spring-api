package gr.aueb.cf.myreserva.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
public record AuthenticationRequestDTO(
        @NotNull
        String email,
        @NotNull
        String password
) {}
