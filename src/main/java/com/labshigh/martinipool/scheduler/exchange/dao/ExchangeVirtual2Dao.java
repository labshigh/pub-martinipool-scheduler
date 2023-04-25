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
public class ExchangeVirtual2Dao {

  private long uid;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime createdAt;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime updatedAt;
  private boolean deletedFlag;
  private boolean usedFlag;

  private String name;
  private String price;
}
