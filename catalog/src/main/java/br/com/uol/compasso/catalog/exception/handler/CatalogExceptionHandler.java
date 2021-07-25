package br.com.uol.compasso.catalog.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.uol.compasso.catalog.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CatalogExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex){

        log.error("Handling MethodArgumentNotValidException");

        return ErrorResponse
                .builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(buildMessage(ex))
                .build();

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public void handleResourceNotFoundException(){

        log.error("Handling ResourceNotFoundException");

        //do nothing
    }

    private String buildMessage(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getFieldError();

        if(fieldError != null) {
            return fieldError.getField() + " " + fieldError.getDefaultMessage();
        }

        return "";
    }

}
