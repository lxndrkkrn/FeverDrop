package org.example.exceptions.domain.server;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class BadGateway extends RuntimeException {
    public BadGateway(String message) {
        super(message);
    }
}
