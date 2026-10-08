package org.example.exceptions.dto;

import java.time.LocalDateTime;

public record ExceptionDTO(

        LocalDateTime timestamp,
        int status,
        String error,
        String message

) {
}
