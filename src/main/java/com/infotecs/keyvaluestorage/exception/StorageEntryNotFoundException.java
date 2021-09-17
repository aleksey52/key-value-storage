package com.infotecs.keyvaluestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StorageEntryNotFoundException extends RuntimeException {
    public StorageEntryNotFoundException() {
        super();
    }
    public StorageEntryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public StorageEntryNotFoundException(String message) {
        super(message);
    }
    public StorageEntryNotFoundException(Throwable cause) {
        super(cause);
    }
}
