package com.labshigh.martinipool.scheduler.common;

public class Constants {
  private Constants() {
    throw new IllegalStateException("Constants Class");
  }

  // 응답 컨텐츠 타입
  public static final String REQUEST_CONTENT_TYPE = "application/json; charset=UTF-8";
  public static final String RESPONSE_CONTENT_TYPE = "application/json; charset=UTF-8";

  // iso 8601 json 날짜 포멧
  public static final String JSONFY_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  public static final String ES_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.Z";
  public static final String VIEW_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String UI_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String TIMEZONE = "Asia/Seoul";

  // 기본 페이지 사이즈
  public static final int DEFAULT_PAGE_SIZE = 20;
  // 최대 페이지 사이즈
  public static final int MAX_LIST_PAGE_SIZE = 250;

  // http status
  public static final String RESPONSE_CODE_CORE_ERROR_MSG = "CORE API Response Error";

  // 이메일 최소/최대 글자수
  public static final int MIN_EMAIL_INPUT_SIZE = 5;
  public static final int MAX_EMAIL_INPUT_SIZE = 256;

  // 비밀번호 최소 글자수
  public static final int MIN_PASSWORD_INPUT_SIZE = 6;

  public static final String MSG_NO_DATA = "데이터가 없습니다..";
  public static final String MSG_DUPLICATED_DATA = "이미 중복된 데이터가 존재합니다.";

  public static final String MSG_NOT_URL = "url 이 잘못 되었습니다.";

  public static final String MSG_WALLET_INSUFFICIENT_BALANCE = "잔액이 부족합니다.";

}
