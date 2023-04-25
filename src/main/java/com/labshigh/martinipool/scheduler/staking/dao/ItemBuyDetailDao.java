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
public class ItemBuyDetailDao {

  private long uid;
  private String userId;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime createdAt;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime updatedAt;
  private boolean deletedFlag;
  private Boolean usedFlag;
  private long memberUid;
  private long itemUid;
  private BigDecimal price;
  private BigDecimal krwPrice;
  private BigDecimal interestPrice;
  private BigDecimal krwInterestPrice;
  private BigDecimal krwEth;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime startAt;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime endAt;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime requestEndAt;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime withdrawalRequestEndAt;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime curWithdrawalRequestEndAt;
  private String round;
  private String closeRound;
  private BigDecimal minPrice;
  private String interest;
  private String remainingDays;

  private String priceUnit;
  private String referrer;

}
