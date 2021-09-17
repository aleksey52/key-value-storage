package com.infotecs.keyvaluestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DumpFileNotFoundException extends RuntimeException {
    public DumpFileNotFoundException() {
        super();
    }

    public DumpFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DumpFileNotFoundException(String message) {
        super(message);
    }

    public DumpFileNotFoundException(Throwable cause) {
        super(cause);
    }
}