package com.labshigh.martinipool.core.utils;

import com.labshigh.martinipool.core.exceptions.ApiContextException;
import com.labshigh.martinipool.core.models.ResponseModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

@Log4j2
public class ResponseUtils {

  /**
   * ResponseModel Error setting
   * @param status
   * @param message
   * @return
   */
  public static ResponseModel makeErrorMessage(int status, String message) {
    log.debug("{} - {}", status, message);
    ResponseModel responseModel = new ResponseModel();
    responseModel.setStatus(status);
    responseModel.setMessage(message);

    if (status > 500) {
      responseModel.error.setErrorCode(status);
      responseModel.error.setErrorMessage(message);
    }

    return responseModel;
  }

  public static ResponseModel createFailResponseModel(ApiContextException exception) {
    ResponseModel responseModel = new ResponseModel();
    responseModel.setStatus(exception.getHttpStatusCode());
    responseModel.setMessage(exception.getMessage());
    return responseModel;
  }

  public static ResponseModel createBadRequestResponseModel(BindingResult bindingResult) {
    ResponseModel responseModel = new ResponseModel();
    responseModel.setStatus(HttpStatus.BAD_REQUEST.value());
    responseModel.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
    return responseModel;
  }
}
