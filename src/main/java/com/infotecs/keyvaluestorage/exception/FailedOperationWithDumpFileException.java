package com.infotecs.keyvaluestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class FailedOperationWithDumpFileException extends RuntimeException {
    public FailedOperationWithDumpFileException() {
        super();
    }
    public FailedOperationWithDumpFileException(String message, Throwable cause) {
        super(message, cause);
    }
    public FailedOperationWithDumpFileException(String message) {
        super(message);
    }
    public FailedOperationWithDumpFileException(Throwable cause) {
        super(cause);
    }
}