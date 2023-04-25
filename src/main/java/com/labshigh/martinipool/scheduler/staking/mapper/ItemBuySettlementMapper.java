package com.labshigh.martinipool.scheduler.staking.mapper;

import com.labshigh.martinipool.scheduler.staking.dao.ItemBuySettlementDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ItemBuySettlementMapper {
  List<ItemBuySettlementDao> list(ItemBuySettlementDao requestModel);
  void updateWithdrawalCompletedFlag(ItemBuySettlementDao itemBuySettlementDao);
}
