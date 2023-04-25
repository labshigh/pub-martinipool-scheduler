package com.labshigh.martinipool.scheduler.exchange.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labshigh.martinipool.scheduler.common.Constants;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExchangeDao {

  private long uid;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime createdAt;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime updatedAt;
  private boolean deletedFlag;
  private boolean usedFlag;

  private String code;
  private String currencyCode;
  private String currencyName;
  private String country;
  private String name;
  private String date;
  private String time;
  private String recurrenceCount;
  private String basePrice;
  private String openingPrice;
  private String highPrice;
  private String lowPrice;
  private String change;
  private String changePrice;
  private String cashBuyingPrice;
  private String cashSellingPrice;
  private String ttBuyingPrice;
  private String ttSellingPrice;
  private String tcBuyingPrice;
  private String fcSellingPrice;
  private String exchangeCommission;
  private String usDollarRate;
  private String high52wPrice;
  private String high52wDate;
  private String low52wPrice;
  private String low52wDate;
  private String currencyUnit;
  private String provider;
  private String signedChangePrice;
  private String signedChangeRate;
  private String changeRate;
}
