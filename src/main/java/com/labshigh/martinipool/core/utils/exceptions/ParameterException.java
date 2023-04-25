package com.labshigh.martinipool.core.utils.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class ParameterException extends RuntimeException {

  @Getter @Setter
  int statusCode = HttpStatus.BAD_REQUEST.value();

  public ParameterException() {
  }

  public ParameterException(String message) {
    super(message);
  }

  public ParameterException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public ParameterException(String message, Throwable cause) {
    super(message, cause);
  }

  public ParameterException(Throwable cause) {
    super(cause);
  }

  public ParameterException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
