package com.labshigh.martinipool.scheduler.staking.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemBuySettlementListRequestModel {


  private long memberUid;
  private long itemUid;

  private String userId;

}
