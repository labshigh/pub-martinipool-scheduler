package com.labshigh.martinipool.scheduler.staking.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ItemListRequestModel {

  private long uid;
  private Boolean usedFlag;
  private Boolean vipFlag;
  private LocalDateTime endAt;
  private String year;
  private String round;


}
