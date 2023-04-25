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
public class ItemBuySettlementDao {

  private long uid;
  private long itemUid;
  private String userId;
  private long memberUid;
  private long itemBuyUid;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime createdAt;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime updatedAt;
  private boolean deletedFlag;
  private Boolean usedFlag;
  private String type;
  private BigDecimal price;
  private boolean withdrawalApprovalFlag;
  private boolean withdrawalCompletedFlag;

  public enum SettlementType {
    DEPOSIT,
    WITHDRAWAL
  }
}
