package org.example.exceptions.domain.server;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INSUFFICIENT_STORAGE)
public class InsufficientStorage extends RuntimeException {
    public InsufficientStorage(String message) {
        super(message);
    }
}
