package com.labshigh.martinipool.core.utils.exceptions;

import com.labshigh.martinipool.core.models.ResponseModel.ErrorModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {

  @Getter @Setter
  int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
  @Getter @Setter
  ErrorModel error = new ErrorModel();

  public ServiceException() {
  }

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public ServiceException(String message, int statusCode, String errorMessage, int errorCode) {
    super(message);
    this.statusCode = statusCode;
    this.error.setErrorCode(errorCode);
    this.error.setErrorMessage(errorMessage);
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceException(Throwable cause) {
    super(cause);
  }

  public ServiceException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
