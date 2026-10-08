package exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ==========================================
    // CLIENT EXCEPTIONS (4xx)
    // ==========================================

    @ExceptionHandler(org.example.exceptions.domain.client.BadRequest.class)
    public ResponseEntity<ExceptionDTO> handleBadRequest(org.example.exceptions.domain.client.BadRequest ex) {
        log.error("Некорректный запрос (400): {}", ex.getMessage());
        return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.client.Unauthorized.class)
    public ResponseEntity<ExceptionDTO> handleUnauthorized(org.example.exceptions.domain.client.Unauthorized ex) {
        log.error("Пользователь не авторизован (401): {}", ex.getMessage());
        return createResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.client.PaymentRequired.class)
    public ResponseEntity<ExceptionDTO> handlePaymentRequired(org.example.exceptions.domain.client.PaymentRequired ex) {
        log.error("Требуется оплата (402): {}", ex.getMessage());
        return createResponse(HttpStatus.PAYMENT_REQUIRED, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.client.Forbidden.class)
    public ResponseEntity<ExceptionDTO> handleForbidden(org.example.exceptions.domain.client.Forbidden ex) {
        log.error("Доступ запрещен (403): {}", ex.getMessage());
        return createResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.client.NotFound.class)
    public ResponseEntity<ExceptionDTO> handleNotFound(org.example.exceptions.domain.client.NotFound ex) {
        log.error("Ресурс не найден (404): {}", ex.getMessage());
        return createResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.client.MethodNotAllowed.class)
    public ResponseEntity<ExceptionDTO> handleMethodNotAllowed(org.example.exceptions.domain.client.MethodNotAllowed ex) {
        log.error("Метод не поддерживается (405): {}", ex.getMessage());
        return createResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.client.RequestTimeout.class)
    public ResponseEntity<ExceptionDTO> handleRequestTimeout(org.example.exceptions.domain.client.RequestTimeout ex) {
        log.error("Истекло время ожидания запроса (408): {}", ex.getMessage());
        return createResponse(HttpStatus.REQUEST_TIMEOUT, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.client.Conflict.class)
    public ResponseEntity<ExceptionDTO> handleConflict(org.example.exceptions.domain.client.Conflict ex) {
        log.error("Конфликт состояний (409): {}", ex.getMessage());
        return createResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.client.Locked.class)
    public ResponseEntity<ExceptionDTO> handleLocked(org.example.exceptions.domain.client.Locked ex) {
        log.error("Ресурс заблокирован (423): {}", ex.getMessage());
        return createResponse(HttpStatus.LOCKED, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.client.TooManyRequests.class)
    public ResponseEntity<ExceptionDTO> handleTooManyRequests(org.example.exceptions.domain.client.TooManyRequests ex) {
        log.error("Слишком много запросов (429): {}", ex.getMessage());
        return createResponse(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.client.im_a_teapot_418.class)
    public ResponseEntity<ExceptionDTO> handleTeapot(org.example.exceptions.domain.client.im_a_teapot_418 ex) {
        log.error("(418)... : {}", ex.getMessage());
        return createResponse(HttpStatus.I_AM_A_TEAPOT, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.client.InvalidToken.class)
    public ResponseEntity<ExceptionDTO> handleInvalidToken(org.example.exceptions.domain.client.InvalidToken ex) {
        log.error("Невалидный токен (498 / 401): {}", ex.getMessage());
        return createResponse(HttpStatus.UNAUTHORIZED, "Ошибка безопасности: " + ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.client.ClientClosedRequest.class)
    public ResponseEntity<ExceptionDTO> handleClientClosedRequest(org.example.exceptions.domain.client.ClientClosedRequest ex) {
        log.error("Клиент закрыл соединение (499): {}", ex.getMessage());
        ExceptionDTO dto = new ExceptionDTO(LocalDateTime.now(), 499, "Client Closed Request", ex.getMessage());
        return new ResponseEntity<>(dto, HttpStatus.valueOf(499));
    }

    // ==========================================
    // SERVER EXCEPTIONS (5xx)
    // ==========================================

    @ExceptionHandler(org.example.exceptions.domain.server.InternalServerError.class)
    public ResponseEntity<ExceptionDTO> handleInternalServerError(org.example.exceptions.domain.server.InternalServerError ex) {
        log.error("Внутренняя ошибка сервера (500): ", ex);
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.server.NotImplemented.class)
    public ResponseEntity<ExceptionDTO> handleNotImplemented(org.example.exceptions.domain.server.NotImplemented ex) {
        log.error("Метод не реализован (501): {}", ex.getMessage());
        return createResponse(HttpStatus.NOT_IMPLEMENTED, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.server.BadGateway.class)
    public ResponseEntity<ExceptionDTO> handleBadGateway(org.example.exceptions.domain.server.BadGateway ex) {
        log.error("Плохой шлюз (502): {}", ex.getMessage());
        return createResponse(HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.server.ServiceUnavailable.class)
    public ResponseEntity<ExceptionDTO> handleServiceUnavailable(org.example.exceptions.domain.server.ServiceUnavailable ex) {
        log.error("Сервис недоступен (503): {}", ex.getMessage());
        return createResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.server.GatewayTimeout.class)
    public ResponseEntity<ExceptionDTO> handleGatewayTimeout(org.example.exceptions.domain.server.GatewayTimeout ex) {
        log.error("Время ожидания от шлюза истекло (504): {}", ex.getMessage());
        return createResponse(HttpStatus.GATEWAY_TIMEOUT, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.server.InsufficientStorage.class)
    public ResponseEntity<ExceptionDTO> handleInsufficientStorage(org.example.exceptions.domain.server.InsufficientStorage ex) {
        log.error("Недостаточно места на сервере (507): {}", ex.getMessage());
        return createResponse(HttpStatus.INSUFFICIENT_STORAGE, ex.getMessage());
    }

    @ExceptionHandler(org.example.exceptions.domain.server.ConnectionTimeOut.class)
    public ResponseEntity<ExceptionDTO> handleConnectionTimeOut(org.example.exceptions.domain.server.ConnectionTimeOut ex) {
        log.error("Таймаут соединения (504 / Gateway Timeout): {}", ex.getMessage());
        return createResponse(HttpStatus.GATEWAY_TIMEOUT, "Ошибка соединения: " + ex.getMessage());
    }

    // ==========================================
    // Creator
    // ==========================================

    private ResponseEntity<ExceptionDTO> createResponse(HttpStatus status, String message) {
        ExceptionDTO dto = new ExceptionDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return new ResponseEntity<>(dto, status);
    }
}
