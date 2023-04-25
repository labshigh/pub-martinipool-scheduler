package com.labshigh.martinipool.scheduler.exchange.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labshigh.martinipool.scheduler.common.Constants;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ExchangeVirtualDao {

  private long uid;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime createdAt;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime updatedAt;
  private boolean deletedFlag;
  private boolean usedFlag;

  private String name;
  private long id;
  private String last;
  private String lowestAsk;
  private String highestBid;
  private String percentChange;
  private String baseVolume;
  private String quoteVolume;
  private String isFrozen;
  private String postOnly;
  private String high24hr;
  private String low24hr;
  private String previousClose;
}
