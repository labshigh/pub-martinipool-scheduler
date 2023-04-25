package com.labshigh.martinipool.scheduler.staking.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class WalletTransactionRequestModel {

  private String toAddress;
  private String toUserId;

  private Long coinId;

  private Long tokenId;

  private String userId;

  private String memo;

  private Double amount;

  private Long refTransactionId;

  private String sendType;

  private String receiveType;

  private String fee;

  private String usd;

  private String extraValue;

  private String lockUpDay;

  private String lockUpAmount;

  private Long memberUid;

}
