package ru.practicum.dto.mainservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("Logging exception {}, status: {}", e.getMessage(), 400);
        return new ResponseEntity<>(new ErrorResponse("400", "Incorrect input data", e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.info("Logging exception {}, status {}", e.getMessage(), 409);
        return new ResponseEntity<>(new ErrorResponse("409", "Integrity constraint has been violated.",
                e.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        log.info("Logging exception {}, status {}", e.getMessage(), 404);
        return new ResponseEntity<>(new ErrorResponse("404", "The required object was not found.",
                e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info("Logging exception {}, status {}", e.getMessage(), 400);
        return new ResponseEntity<>(new ErrorResponse("400", "Incorrectly made request", e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleConditionalsAreNotMet(ConditionsAreNotMet e) {
        log.info("Logging exception {}, status {}", e.getMessage(), 409);
        return new ResponseEntity<>(new ErrorResponse("409",
                "For the requested operation the conditions are not met.",
                e.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleIncorrectInputArguments(IncorrectInputArguments e) {
        log.info("Logging incorrect input arguments {}, status {}", e.getMessage(), 400);
        return new ResponseEntity<>(new ErrorResponse("400", "Incorrect input arguments",
                e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
