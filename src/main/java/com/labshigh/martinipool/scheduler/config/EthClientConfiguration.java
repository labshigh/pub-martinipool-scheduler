package com.labshigh.martinipool.scheduler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class EthClientConfiguration {

  @Value("${ethereum.url}")
  private String url;
  @Value("${ethereum.token}")
  private String token;

  @Bean
  public Web3j web3j() {
    Web3j web3j = Web3j.build(new HttpService(url));

    return web3j;
  }
}
