package com.labshigh.martinipool.scheduler.staking.mapper;

import com.labshigh.martinipool.scheduler.staking.dao.MemberDao;
import com.labshigh.martinipool.scheduler.staking.dao.WithdrawDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface WithdrawMapper {

    void insert(WithdrawDao withdrawDao);
    List<WithdrawDao> list();
    void updateScheduleStatus(WithdrawDao withdrawDao);
    void updateTxHash(WithdrawDao withdrawDao);
    MemberDao getMember(MemberDao memberDao);
    MemberDao getWalletMember(MemberDao memberDao);
    void updateTransactionId(WithdrawDao withdrawDao);
}
