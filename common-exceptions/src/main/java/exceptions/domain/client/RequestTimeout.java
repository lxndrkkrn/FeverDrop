package org.example.exceptions.domain.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
public class RequestTimeout extends RuntimeException {
    public RequestTimeout(String message) {
        super(message);
    }
}
