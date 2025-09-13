package gr.aueb.cf.myreserva.dto;

import gr.aueb.cf.myreserva.dto.user.UserReadOnlyDTO;
import org.springframework.lang.Nullable;

/**
 * This container was created so we could mirror the Node backend response and allow the frontend to work with both APIs
 * The frontend expects a json containing "user" and "accessToken"
 */
public record ApiAuthResponse(
        boolean status,
        String message,
        @Nullable
        UserReadOnlyDTO user,
        @Nullable
        String accessToken
) {}