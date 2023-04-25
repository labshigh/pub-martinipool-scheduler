package com.labshigh.martinipool.core.exceptions;

import org.springframework.http.HttpStatus;

public class ApiContextException extends RuntimeException {
  private final int httpStatusCode;

  public ApiContextException(int httpStatusCode, String message) {
    super(message);
    this.httpStatusCode = httpStatusCode;
  }

  public ApiContextException(String message, Throwable cause) {
    super(message, cause);
    this.httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

  public ApiContextException(Throwable cause) {
    super(cause);
    this.httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

  public int getHttpStatusCode() {
    return this.httpStatusCode;
  }
}
