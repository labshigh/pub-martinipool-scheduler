package com.labshigh.martinipool.scheduler.exchange.mapper;

import com.labshigh.martinipool.scheduler.exchange.dao.ExchangeVirtual2Dao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ExchangeVirtual2Mapper {

  void insert(ExchangeVirtual2Dao exchangeVirtual2Dao);

}
