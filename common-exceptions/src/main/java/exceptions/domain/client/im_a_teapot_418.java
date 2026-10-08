package org.example.exceptions.domain.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class im_a_teapot_418 extends RuntimeException {
    public im_a_teapot_418(String message) {
        super(message);
    }
}
