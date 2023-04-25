package com.labshigh.martinipool.scheduler.web3.mapper;

import com.labshigh.martinipool.scheduler.web3.dao.MarketItemDao;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MarketItemMapper {

  List<MarketItemDao> gets(MarketItemDao marketItemDao);

  void update(MarketItemDao marketItemDao);

}
