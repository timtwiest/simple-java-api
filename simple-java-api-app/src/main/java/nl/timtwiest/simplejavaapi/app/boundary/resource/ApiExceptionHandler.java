package nl.timtwiest.simplejavaapi.app.boundary.resource;

import nl.timtwiest.simplejavaapi.api.models.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        String detail = "Invalid value for parameter '%s'".formatted(exception.getName());
        return errorResponse(HttpStatus.BAD_REQUEST, "Bad request", List.of(detail));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableMessage() {
        return errorResponse(HttpStatus.BAD_REQUEST, "Bad request", List.of("Malformed request body"));
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ErrorResponse> handleFrameworkError(ErrorResponseException exception) {
        HttpStatus status = HttpStatus.resolve(exception.getStatusCode().value());
        String message = status == null ? "Request failed" : status.getReasonPhrase();
        return errorResponse(exception.getStatusCode(), message, List.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleMissingResource(NoResourceFoundException exception) {
        return errorResponse(exception.getStatusCode(), "Not Found", List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedError(Exception exception) {
        LOGGER.error("Unhandled API error", exception);
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", List.of());
    }

    private ResponseEntity<ErrorResponse> errorResponse(HttpStatusCode status, String message, List<String> errors) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), message, errors));
    }
}
