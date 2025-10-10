package learn.tdd.api.controller;

import learn.tdd.commandmodel.InvalidCommandException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidCommandExceptionHandler {

    @ExceptionHandler(InvalidCommandException.class)
    public ResponseEntity<?> handleInvalidCommandException(InvalidCommandException e) {
        return ResponseEntity.badRequest().build();
    }
}
