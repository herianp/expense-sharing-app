package com.herian.expensesharingapp.exception;

import com.herian.expensesharingapp.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessageDto> handleRuntimeException(RuntimeException ex) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto();
        errorMessageDto.setCode(HttpStatus.BAD_REQUEST.value()); // or the appropriate status code
        errorMessageDto.setErrorMessage(ex.getMessage());

        return new ResponseEntity<>(errorMessageDto, HttpStatus.BAD_REQUEST);
    }
}
