package com.labshigh.martinipool.scheduler.web3.scheduler;

import com.labshigh.martinipool.scheduler.web3.service.Web3Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class Web3Scheduler {

  private final Web3Service web3Service;

  public Web3Scheduler(Web3Service web3Service) {
    this.web3Service = web3Service;
  }

//  @Scheduled(fixedDelay = 20000)
  public void transactionReceipt() throws Exception {
    log.debug("TransactionReceipt Start with : {}", Thread.currentThread().getName());

    web3Service.transactionReceipt();

    log.debug("TransactionReceipt End with : {}", Thread.currentThread().getName());
  }
}
