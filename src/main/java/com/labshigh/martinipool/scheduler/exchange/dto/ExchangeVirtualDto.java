package com.labshigh.martinipool.scheduler.exchange.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeVirtualDto {

  private currencyModel USDT_BTC;
  private currencyModel USDT_ETH;

  @Getter
  @Setter
  private class currencyModel {

    private long id;
    private String last;
    private String lowestAsk;
    private String highestBid;
    private String percentChange;
    private String baseVolume;
    private String quoteVolume;
    private String isFrozen;
    private String postOnly;
    private String high24hr;
    private String low24hr;
  }
}
