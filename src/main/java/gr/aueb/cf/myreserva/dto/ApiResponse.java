package gr.aueb.cf.myreserva.dto;

import org.springframework.lang.Nullable;

public record ApiResponse<T>(
        boolean status,
        String message,
        @Nullable
        T data
) {}