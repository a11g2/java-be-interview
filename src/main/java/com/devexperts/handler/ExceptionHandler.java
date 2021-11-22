package com.devexperts.handler;

import com.devexperts.exception.AccountNotFoundException;
import com.devexperts.exception.BalanceNotEnoughException;
import com.devexperts.exception.TransferFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
            (value = {AccountNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException runtimeException, WebRequest webRequest) {
        return handleExceptionInternal(runtimeException, runtimeException.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
            (value = {BalanceNotEnoughException.class, TransferFailedException.class})
    protected ResponseEntity<Object> handleTransferFail(RuntimeException runtimeException, WebRequest webRequest) {
        return handleExceptionInternal(runtimeException, runtimeException.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }
}
