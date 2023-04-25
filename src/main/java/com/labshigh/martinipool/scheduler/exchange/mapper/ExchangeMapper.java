package com.labshigh.martinipool.scheduler.exchange.mapper;

import com.labshigh.martinipool.scheduler.exchange.dao.ExchangeDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ExchangeMapper {

  void insert(ExchangeDao exchangeDAO);

}
