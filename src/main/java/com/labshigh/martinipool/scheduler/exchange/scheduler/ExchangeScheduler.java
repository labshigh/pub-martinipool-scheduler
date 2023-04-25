package com.labshigh.martinipool.scheduler.exchange.scheduler;

import com.labshigh.martinipool.scheduler.exchange.service.ExchangeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ExchangeScheduler {

  private final ExchangeService exchangeService;

  public ExchangeScheduler(ExchangeService exchangeService) {
    this.exchangeService = exchangeService;
  }

//  @Scheduled(cron = "0 */30 * * * *")
  public void updateCurrency() {

    exchangeService.updateCurrency();
  }

//  @Scheduled(cron = "0 */30 * * * *")
  public void updateVirtualCurrency() {

    exchangeService.updateVirtualCurrency();
  }

//    @Scheduled(cron = "0 */30 * * * *")
  public void updateExchangeVirtual() {

    exchangeService.updateExchangeVirtual();
  }


//  @Scheduled(cron = "0 */5 * * * *")
  public void updateFognet() {

    exchangeService.updateFognet();
  }
}
