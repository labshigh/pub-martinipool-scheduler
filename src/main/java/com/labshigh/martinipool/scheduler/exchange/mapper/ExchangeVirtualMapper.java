package com.labshigh.martinipool.scheduler.exchange.mapper;

import com.labshigh.martinipool.scheduler.exchange.dao.ExchangeVirtualDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ExchangeVirtualMapper {

  void insert(ExchangeVirtualDao exchangeVirtualDao);

}
