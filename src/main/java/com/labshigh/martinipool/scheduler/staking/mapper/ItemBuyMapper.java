package com.labshigh.martinipool.scheduler.staking.mapper;

import com.labshigh.martinipool.scheduler.staking.dao.ItemBuyDao;
import com.labshigh.martinipool.scheduler.staking.dao.ItemBuyDetailDao;
import com.labshigh.martinipool.scheduler.staking.model.request.ItemBuyListRequestModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ItemBuyMapper {

  void insert(ItemBuyDao dao);
  void updateAutoProgressFlag(ItemBuyDao dao);
  ItemBuyDetailDao get(ItemBuyListRequestModel requestModel);
  List<ItemBuyDetailDao> list(ItemBuyListRequestModel requestModel);

  List<ItemBuyDetailDao> list(ItemBuyDetailDao itemBuyDetailDao);
  List<ItemBuyDetailDao> listMemberUidSum(ItemBuyDetailDao itemBuyDetailDao);
  int count(@Param(value = "request") ItemBuyListRequestModel requestModel);

}
