package com.labshigh.martinipool.scheduler.exchange.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.labshigh.martinipool.core.helper.WebClientHelper;
import com.labshigh.martinipool.core.inherits.AbstractRestService;
import com.labshigh.martinipool.scheduler.exchange.dao.ExchangeDao;
import com.labshigh.martinipool.scheduler.exchange.dao.ExchangeVirtual2Dao;
import com.labshigh.martinipool.scheduler.exchange.dao.ExchangeVirtualDao;
import com.labshigh.martinipool.scheduler.exchange.mapper.ExchangeMapper;
import com.labshigh.martinipool.scheduler.exchange.mapper.ExchangeVirtual2Mapper;
import com.labshigh.martinipool.scheduler.exchange.mapper.ExchangeVirtualMapper;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ExchangeService extends AbstractRestService {

  private static String api = "https://quotation-api-cdn.dunamu.com/v1/forex/recent?codes=FRX.KRWUSD";
  private static String poloniexApi = "https://poloniex.com/public?command=returnTicker";
  private final String yahooFinanceApi = "https://query1.finance.yahoo.com/v8/finance/chart/";
  private final String fognetApi = "https://sapi.coincarp.com/api/v1/his/coin/trend?code=fognet&type=d";
  private final ExchangeMapper exchangeMapper;
  private final ExchangeVirtualMapper exchangeVirtualMapper;
  private final ExchangeVirtual2Mapper exchangeVirtual2Mapper;

  public ExchangeService(ExchangeMapper exchangeMapper, ExchangeVirtualMapper exchangeVirtualMapper,
      ExchangeVirtual2Mapper exchangeVirtual2Mapper) {
    this.exchangeMapper = exchangeMapper;
    this.exchangeVirtualMapper = exchangeVirtualMapper;
    this.exchangeVirtual2Mapper = exchangeVirtual2Mapper;
  }

  public void updateCurrency() {

    URI uri = makeUri(api);

    String jsonString = WebClientHelper.getInstance().get(uri, String.class);

    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser.parse(jsonString);

    log.debug(jsonElement);

    for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
      ExchangeDao exchangeDao = ExchangeDao.builder()
          .code(jsonElement1.getAsJsonObject().get("code").getAsString())
          .currencyCode(
              jsonElement1.getAsJsonObject().get("currencyCode")
                  .getAsString())
          .currencyName(
              jsonElement1.getAsJsonObject().get("currencyName")
                  .getAsString())
          .country(
              jsonElement1.getAsJsonObject().get("country").getAsString())
          .name(jsonElement1.getAsJsonObject().get("name").getAsString())
          .date(jsonElement1.getAsJsonObject().get("date").getAsString())
          .time(jsonElement1.getAsJsonObject().get("time").getAsString())
          .recurrenceCount(
              jsonElement1.getAsJsonObject().get("recurrenceCount")
                  .getAsString())
          .basePrice(
              jsonElement1.getAsJsonObject().get("basePrice").getAsString())
          .openingPrice(
              jsonElement1.getAsJsonObject().get("openingPrice")
                  .getAsString())
          .highPrice(
              jsonElement1.getAsJsonObject().get("highPrice").getAsString())
          .lowPrice(
              jsonElement1.getAsJsonObject().get("lowPrice").getAsString())
          .change(jsonElement1.getAsJsonObject().get("change").getAsString())
          .changePrice(
              jsonElement1.getAsJsonObject().get("changePrice")
                  .getAsString())
          .cashBuyingPrice(
              jsonElement1.getAsJsonObject().get("cashBuyingPrice")
                  .getAsString())
          .cashSellingPrice(
              jsonElement1.getAsJsonObject().get("cashSellingPrice")
                  .getAsString())
          .ttBuyingPrice(jsonElement1.getAsJsonObject().get("ttBuyingPrice")
              .getAsString())
          .ttSellingPrice(
              jsonElement1.getAsJsonObject().get("ttSellingPrice")
                  .getAsString())
//        .tcBuyingPrice(jsonElement1.getAsJsonObject().get("tcBuyingPrice").getAsString())
//        .fcSellingPrice(jsonElement1.getAsJsonObject().get("fcSellingPrice").getAsString())
          .exchangeCommission(
              jsonElement1.getAsJsonObject().get("exchangeCommission")
                  .getAsString())
          .usDollarRate(
              jsonElement1.getAsJsonObject().get("usDollarRate")
                  .getAsString())
          .high52wPrice(
              jsonElement1.getAsJsonObject().get("high52wPrice")
                  .getAsString())
          .high52wDate(
              jsonElement1.getAsJsonObject().get("high52wDate")
                  .getAsString())
          .low52wPrice(
              jsonElement1.getAsJsonObject().get("low52wPrice")
                  .getAsString())
          .low52wDate(
              jsonElement1.getAsJsonObject().get("low52wDate").getAsString())
          .currencyUnit(
              jsonElement1.getAsJsonObject().get("currencyUnit")
                  .getAsString())
          .provider(
              jsonElement1.getAsJsonObject().get("provider").getAsString())
          .signedChangePrice(
              jsonElement1.getAsJsonObject().get("signedChangePrice")
                  .getAsString())
          .signedChangeRate(
              jsonElement1.getAsJsonObject().get("signedChangeRate")
                  .getAsString())
          .changeRate(
              jsonElement1.getAsJsonObject().get("changeRate").getAsString())
          .build();

      exchangeMapper.insert(exchangeDao);
    }
  }

  public void updateVirtualCurrency() {

    URI uri = makeUri(poloniexApi);

    String currencyString = WebClientHelper.getInstance().get(uri, String.class);

    log.debug(currencyString.replaceAll("\n", ""));
    log.debug(currencyString.replaceAll("\n", ""));

    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser.parse(currencyString.replaceAll("\n", ""));

    for (String currencyName : jsonElement.getAsJsonObject().keySet()) {
      log.debug(currencyName);

      ExchangeVirtualDao exchangeVirtualDao = ExchangeVirtualDao.builder()
          .name(currencyName)
          .id(jsonElement.getAsJsonObject().getAsJsonObject(currencyName).get("id").getAsLong())
          .last(
              jsonElement.getAsJsonObject().getAsJsonObject(currencyName).get("last").getAsString())
          .lowestAsk(jsonElement.getAsJsonObject().getAsJsonObject(currencyName).get("lowestAsk")
              .getAsString())
          .highestBid(jsonElement.getAsJsonObject().getAsJsonObject(currencyName).get("highestBid")
              .getAsString())
          .percentChange(
              jsonElement.getAsJsonObject().getAsJsonObject(currencyName).get("percentChange")
                  .getAsString())
          .baseVolume(jsonElement.getAsJsonObject().getAsJsonObject(currencyName).get("baseVolume")
              .getAsString())
          .quoteVolume(
              jsonElement.getAsJsonObject().getAsJsonObject(currencyName).get("quoteVolume")
                  .getAsString())
          .isFrozen(jsonElement.getAsJsonObject().getAsJsonObject(currencyName).get("isFrozen")
              .getAsString())
          .postOnly(jsonElement.getAsJsonObject().getAsJsonObject(currencyName).get("postOnly")
              .getAsString())
          .high24hr(jsonElement.getAsJsonObject().getAsJsonObject(currencyName).get("high24hr")
              .getAsString())
          .low24hr(jsonElement.getAsJsonObject().getAsJsonObject(currencyName).get("low24hr")
              .getAsString())
          .build();

      exchangeVirtualMapper.insert(exchangeVirtualDao);
    }
  }

  public void updateExchangeVirtual() {
    ArrayList<String> codeArrayList = new ArrayList<>(Arrays.asList( "USDT-USD","ETH-KRW","ETH-KRW","ETH-USD","DAI-KRW","DAI-USD","USDT-KRW","USDT-USD","USDC-KRW","USDC-USD"));
    codeArrayList.forEach(item->{
      URI uri = makeUri(yahooFinanceApi + item);

      String string = WebClientHelper.getInstance().get(uri, String.class);
      JsonParser parser = new JsonParser();
      JsonElement jsonElement = parser.parse(string);

      log.debug(jsonElement);
      JsonElement jsonData = jsonElement.getAsJsonObject().get("chart").getAsJsonObject()
        .get("result").getAsJsonArray().get(0).getAsJsonObject().get("meta");

      log.debug(jsonData);

      exchangeVirtualMapper.insert(ExchangeVirtualDao.builder()
        .name(item)
        .last(jsonData.getAsJsonObject().get("regularMarketPrice").getAsString())
        .previousClose(jsonData.getAsJsonObject().get("previousClose").getAsString())
        .build());
    });
  }

  public void updateFognet() {

    URI uri = makeUri(fognetApi);

    String jsonString = WebClientHelper.getInstance().get(uri, String.class);

    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser.parse(jsonString);

    log.debug(jsonElement);

    int arraySize = jsonElement.getAsJsonObject().get("data").getAsJsonArray().size();

    JsonArray jsonArray = jsonElement.getAsJsonObject().get("data").getAsJsonArray()
        .get(arraySize - 1).getAsJsonArray();

    log.debug(jsonArray);

    exchangeVirtual2Mapper.insert(ExchangeVirtual2Dao.builder()
        .price(jsonArray.get(1).getAsString())
        .name("FOGnet_USD")
        .build());


  }
}
