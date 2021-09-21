package com.infotecs.keyvaluestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class FailedOperationWithDumpFileException extends RuntimeException {

  public FailedOperationWithDumpFileException(String message) {
    super(message);
  }
}