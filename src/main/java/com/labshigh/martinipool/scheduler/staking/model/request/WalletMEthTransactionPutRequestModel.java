package com.labshigh.martinipool.scheduler.staking.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class WalletMEthTransactionPutRequestModel {

  // mEth 지급 타입
  private Long methType;
  // meth 사용 수량
  private BigDecimal methUseAmount;
  // 지급 사유
  private String memo;
  // memberUid
  private Long memberUid;
  // walletId
  private Long walletId;
  private Long itemBuyUid;
  private Long itemUid;

}
