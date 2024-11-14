package ru.practicum.dto.mainservice.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class, IncorrectInputArguments.class,
            MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(Exception e) {
        log.warn("Logging exception {}, status: {}", e.getMessage(), 400);
        return new ResponseEntity<>(new ErrorResponse("400", e.getMessage(),"Check you input data"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConditionsAreNotMet.class})
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(Exception e) {
        log.warn("Logging exception {}, status {}", e.getMessage(), 409);
        return new ResponseEntity<>(new ErrorResponse("409", "Integrity constraint has been violated.",
                e.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler({EntityNotFoundException.class, EventStatusInvalid.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(Exception e) {
        log.warn("Logging exception {}, status {}", e.getMessage(), 404);
        return new ResponseEntity<>(new ErrorResponse("404", "The required object was not found.",
                e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.warn("Logging exception {}, status {}", e.getMessage(), 500);
        return new ResponseEntity<>(new ErrorResponse("500", e.getMessage(), "Error"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
