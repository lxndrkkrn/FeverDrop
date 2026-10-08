package org.example.exceptions.domain.server;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ConnectionTimeOut extends RuntimeException {
    public ConnectionTimeOut(String message) {
        super(message);
    }
}
