package com.infotecs.keyvaluestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class StorageEntryIllegalArgumentException extends RuntimeException {

  public StorageEntryIllegalArgumentException(String message) {
    super(message);
  }
}