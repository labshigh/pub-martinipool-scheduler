package com.labshigh.martinipool.scheduler.staking.scheduler;

import com.labshigh.martinipool.scheduler.exchange.service.ExchangeService;
import com.labshigh.martinipool.scheduler.staking.service.StakingService;
import com.labshigh.martinipool.scheduler.staking.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StakingScheduler {

  private final StakingService stakingService;
  private final WithdrawService withdrawService;


  @Scheduled(cron = "0 0 0/1 * * *")
//  @Scheduled(cron = "* * * * * *")
  public void updateStaking() {

    stakingService.updateStaking();
  }

  @Scheduled(cron = "0 0/1 * * * *")
//  @Scheduled(cron = "* * * * * *")
  public void walletWithdraw() {

    withdrawService.walletWithdraw();
  }

  @Scheduled(cron = "0 0/10 * * * *")
//  @Scheduled(cron = "* * * * * *")
  public void masterWalletBalance() {

    withdrawService.masterWalletBalance();
  }

}
