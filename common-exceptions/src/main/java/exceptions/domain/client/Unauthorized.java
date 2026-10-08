package org.example.exceptions.domain.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class Unauthorized extends RuntimeException {
    public Unauthorized(String message) {
        super(message);
    }
}
