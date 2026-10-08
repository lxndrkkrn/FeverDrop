package org.example.exceptions.domain.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ClientClosedRequest extends RuntimeException {
    public ClientClosedRequest(String message) {
        super(message);
    }
}
