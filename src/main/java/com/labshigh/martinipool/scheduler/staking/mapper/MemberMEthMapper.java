package com.labshigh.martinipool.scheduler.staking.mapper;

import com.labshigh.martinipool.scheduler.staking.dao.MemberMEthDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MemberMEthMapper {

  void insertMemberMEth(MemberMEthDao memberMEthDao );
}
