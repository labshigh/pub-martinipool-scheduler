package com.labshigh.martinipool.scheduler.staking.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
public class WalletTransactionDao {

  private Long transactionId;
  private String userId;
  // 토큰 고유 번호
  private Long tokenId;
  // Status
  private String status;
  // 지급 수량
  private Double amount;
  // 수수료
  private String fee;
  // balance
  private Double balance;
  // txHash
  private String txHash;
  private String memo;
  private String toAddress;
  private String toUserId;
  private LocalDateTime regDate;
  // 지급 타입 ( 서비스 정의 )
  private String type;
  private Long refTransactionId;
  private String fromAddress;
  private String feeSymbol;
  private Long feeTokenId;
  private Double priceKrw;
  private Double priceUsd;
  private String innerTx;
  private Long memberUid;
}
