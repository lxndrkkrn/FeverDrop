package org.example.exceptions.domain.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class PaymentRequired extends RuntimeException {
    public PaymentRequired(String message) {
        super(message);
    }
}
