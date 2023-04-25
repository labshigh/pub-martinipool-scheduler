package com.labshigh.martinipool.core.models;

import com.labshigh.martinipool.core.utils.JsonUtils;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseModel {

  public static class ErrorModel {
    @Getter @Setter
    private int errorCode;
    @Getter @Setter
    private String errorMessage;
    @Getter @Setter
    private Object object;
  }

  long startTime;

  @Getter @Setter
  private int status;
  @Getter @Setter
  private String message;
  @Getter @Setter
  private Object data;

  @Getter
  public ErrorModel error = new ErrorModel();

  public ResponseModel() {
    reset();
  }

  public static ResponseModel create(Object object) {
    ResponseModel responseModel = new ResponseModel();
    responseModel.setData(object);
    return responseModel;
  }

  public ResponseEntity<String> toResponse() {
    return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(toJson());
  }

  // json string to object
  public <T> T getDataToObject(Class<T> valueType) {
    return JsonUtils.convertJsonStringToObject(JsonUtils.convertObjectToJsonString(data), valueType);
  }

  public String toJson() {

    long endTime = System.currentTimeMillis();

    HashMap<String, Object> result = new HashMap<>();

    result.put("status", status);
    result.put("message", message);

    if(data != null) {
      result.put("data", data);
    }

    if(status >= 500) {
      result.put("error", error);
    }

    result.put("executeTime", (endTime - startTime));

    return JsonUtils.convertObjectToJsonString(result);
  }

  public void reset() {
    this.status = 200;
    this.message = "Success";
    this.data = null;
    startTime = System.currentTimeMillis();
  }

  public boolean validStatus() {
    return this.status == 200;
  }
}
