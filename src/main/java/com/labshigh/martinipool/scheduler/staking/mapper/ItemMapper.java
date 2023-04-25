package com.labshigh.martinipool.scheduler.staking.mapper;

import com.labshigh.martinipool.scheduler.staking.dao.ItemDao;
import com.labshigh.martinipool.scheduler.staking.model.request.ItemListRequestModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ItemMapper {


  void updateItem(ItemDao dao);
  void updateSettlementFlag(ItemDao dao);

  void updateSort();

  void updateSortByAdmin(ItemDao dao);

  List<ItemDao> list(ItemListRequestModel itemListRequestModel);
  ItemDao get(ItemListRequestModel itemListRequestModel);

  void insertBuyItem(ItemDao dao);


}
