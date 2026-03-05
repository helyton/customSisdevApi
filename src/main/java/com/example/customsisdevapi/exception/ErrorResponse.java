package com.example.customsisdevapi.exception;

import java.time.OffsetDateTime;

public record ErrorResponse(
        int status,
        String message,
        OffsetDateTime timestamp,
        String path
) {
}
