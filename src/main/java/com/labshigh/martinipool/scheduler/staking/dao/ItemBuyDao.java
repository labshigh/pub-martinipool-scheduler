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
public class ItemBuyDao {

  private long uid;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime createdAt;
  @JsonFormat(pattern = Constants.JSONFY_DATE_FORMAT)
  private LocalDateTime updatedAt;
  private boolean deletedFlag;
  private Boolean usedFlag;
  private long itemUid;
  private long marketItemUid;
  private BigDecimal price;
  private long memberUid;
  private String priceUnit;
  private boolean autoProgressFlag;
  private BigDecimal withdrawalTotalPrice;
  private BigDecimal depositTotalPrice;
  private String userId;


}
