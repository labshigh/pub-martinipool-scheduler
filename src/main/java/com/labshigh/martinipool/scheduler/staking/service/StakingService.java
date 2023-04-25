package com.labshigh.martinipool.scheduler.staking.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.labshigh.martinipool.core.helper.WebClientHelper;
import com.labshigh.martinipool.core.inherits.AbstractRestService;
import com.labshigh.martinipool.core.utils.StringUtils;
import com.labshigh.martinipool.core.utils.exceptions.ServiceException;
import com.labshigh.martinipool.scheduler.exchange.dao.ExchangeDao;
import com.labshigh.martinipool.scheduler.exchange.dao.ExchangeVirtual2Dao;
import com.labshigh.martinipool.scheduler.exchange.dao.ExchangeVirtualDao;
import com.labshigh.martinipool.scheduler.exchange.mapper.ExchangeMapper;
import com.labshigh.martinipool.scheduler.exchange.mapper.ExchangeVirtual2Mapper;
import com.labshigh.martinipool.scheduler.exchange.mapper.ExchangeVirtualMapper;
import com.labshigh.martinipool.scheduler.staking.dao.*;
import com.labshigh.martinipool.scheduler.staking.mapper.*;
import com.labshigh.martinipool.scheduler.staking.model.request.ItemBuyInsertRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.request.ItemBuyListRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.request.ItemListRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.request.WalletMEthTransactionPutRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.request.WalletTransactionPutRequestModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class StakingService extends AbstractRestService {
  private final ItemMapper itemMapper;
  private final ItemBuyMapper itemBuyMapper;
  private final ItemBuySettlementMapper itemBuySettlementMapper;

  private final TransactionHistoryMapper transactionHistoryMapper;
  private final MemberWalletMapper memberWalletMapper;
  private final MemberWalletService memberWalletService;
  private final ItemStakingService itemStakingService;



//  @Transactional(rollbackFor = {Exception.class})
  public void updateStaking() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime endAt = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), 0, 0);

    // 스테이킹 상품 조회
    List<ItemDao> itemDaoList = itemMapper.list(ItemListRequestModel.builder().endAt(endAt).build());
    log.debug(itemDaoList);

    itemDaoList.forEach(itemDao -> {

      try {
        itemStakingService.itemStaking(itemDao);
      } catch (Exception e) {
        log.error(e.getMessage());
      }
      itemMapper.updateSettlementFlag(ItemDao.builder().uid(itemDao.getUid()).settlementFlag(true).build());

    });
  }
}
