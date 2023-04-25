package com.labshigh.martinipool.scheduler.staking.mapper;

import com.labshigh.martinipool.scheduler.staking.dao.MemberWalletDao;
import com.labshigh.martinipool.scheduler.staking.dao.WalletTransactionDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MemberWalletMapper {

  void insertMemberWallet(MemberWalletDao memberWalletDao );
  void insertWalletTransaction(WalletTransactionDao walletTransactionDao );
  void updateWalletAddress(MemberWalletDao memberWalletDao );
  void updateWalletBalance(MemberWalletDao memberWalletDao );
  void updateWalletMigEth(MemberWalletDao memberWalletDao );
  void updateWalletMigStaking(MemberWalletDao memberWalletDao );
  void updateMEth(MemberWalletDao memberWalletDao );
  void updateReferrer(MemberWalletDao memberWalletDao );
  MemberWalletDao get(MemberWalletDao memberWalletDao);
  List<MemberWalletDao> getList(MemberWalletDao memberWalletDao);

}
