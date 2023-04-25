package com.labshigh.martinipool.scheduler.staking.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labshigh.martinipool.scheduler.common.Constants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberWalletDao {

  private long uid;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime createdAt;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime updatedAt;
  private boolean deletedFlag;
  private boolean usedFlag;
  private Long coinId;
  private Long tokenId;
  private String name;
  private String address;
  private Long accountId;
  private Long memberUid;

  private BigDecimal mEth;
  private BigDecimal balance;

  private String userId;
  private String referrerCode;
  private String referrer;

  private BigDecimal migEth;
  private BigDecimal migStaking;

}