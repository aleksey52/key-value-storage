package com.infotecs.keyvaluestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DumpFileNotFoundException extends RuntimeException {
    public DumpFileNotFoundException(String message) {
        super(message);
    }
}