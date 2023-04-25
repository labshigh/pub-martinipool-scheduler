package com.labshigh.martinipool.core.utils.exceptions;

import com.labshigh.martinipool.core.models.ResponseModel.ErrorModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class CoreException extends RuntimeException {

  @Getter @Setter
  int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
  @Getter @Setter
  ErrorModel error = new ErrorModel();

  public CoreException() {
  }

  public CoreException(String message) {
    super(message);
  }

  public CoreException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public CoreException(String message, int statusCode, String errorMessage, int errorCode) {
    super(message);
    this.statusCode = statusCode;
    this.error.setErrorCode(errorCode);
    this.error.setErrorMessage(errorMessage);
  }

  public CoreException(String message, int statusCode, ErrorModel error) {
    super(message);
    this.statusCode = statusCode;
    this.error = error;
  }

  public CoreException(String message, Throwable cause) {
    super(message, cause);
  }

  public CoreException(Throwable cause) {
    super(cause);
  }

  public CoreException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
