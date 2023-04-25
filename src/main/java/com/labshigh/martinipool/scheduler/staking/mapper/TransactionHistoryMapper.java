package com.labshigh.martinipool.scheduler.staking.mapper;

import com.labshigh.martinipool.scheduler.staking.dao.TransactionHistoryDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TransactionHistoryMapper {
  void insert(TransactionHistoryDao dao);

}
