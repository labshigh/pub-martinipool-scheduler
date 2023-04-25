package com.labshigh.martinipool.scheduler.staking.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemBuyInsertRequestModel {

  private long memberUid;
  private String userId;
  private long itemUid;
  private long walletId;
  private long marketItemUid;
  private BigDecimal price;
  private PriceUnit priceUnit;

  //private int itemKind;

  public enum PriceUnit {
    ETH,
    mETH
  }
}


