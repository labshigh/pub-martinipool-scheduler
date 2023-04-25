package com.labshigh.martinipool.scheduler.staking.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class WalletTransactionPutRequestModel {


  private String userId;

  private Long tokenId;

  private Long coinId;

  private String type;

  private Double amount;

  private String memo;

  private String fee;

  private boolean event;

  private Double lockAmount;

  private Long memberUid;

}
