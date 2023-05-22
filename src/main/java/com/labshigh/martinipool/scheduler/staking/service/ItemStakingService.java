package com.labshigh.martinipool.scheduler.staking.service;

import com.labshigh.martinipool.core.inherits.AbstractRestService;
import com.labshigh.martinipool.core.utils.StringUtils;
import com.labshigh.martinipool.core.utils.exceptions.ServiceException;
import com.labshigh.martinipool.scheduler.staking.dao.*;
import com.labshigh.martinipool.scheduler.staking.mapper.*;
import com.labshigh.martinipool.scheduler.staking.model.request.ItemBuyInsertRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.request.ItemBuyListRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.request.ItemListRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.request.WalletTransactionPutRequestModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ItemStakingService extends AbstractRestService {
  private final ItemMapper itemMapper;
  private final ItemBuyMapper itemBuyMapper;
  private final ItemBuySettlementMapper itemBuySettlementMapper;

  private final TransactionHistoryMapper transactionHistoryMapper;
  private final MemberWalletMapper memberWalletMapper;
  private final MemberWalletService memberWalletService;

  @Transactional
  public void itemStaking(ItemDao itemDao) {
    List<WalletTransactionPutRequestModel> walletTransactionPutRequestModelList = new ArrayList<>();
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
    DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
    String nowFormat = now.format(formatter) +":00:00";
    LocalDateTime endAt = LocalDateTime.of(now.getYear(),now.getMonth(), now.getDayOfMonth(), now.getHour(),0,0);
    String year = Integer.toString(now.getYear());
    String nextYear = Integer.toString(now.plusYears(1).getYear());
    try{
      // 지갑 트랜잭션 리스트
      // 스테이킹 상품 구매자 조회
      List<ItemBuyDetailDao> itemBuyDetailDaoList = itemBuyMapper.list(ItemBuyDetailDao.builder().itemUid(itemDao.getUid()).build());
      // 이자 계산
      // 스테이킹 정산 리스트 조회

      itemBuyDetailDaoList.forEach(itemBuyDetailDao -> {


        MemberWalletDao memberWalletDao = memberWalletMapper.get(MemberWalletDao.builder().userId(itemBuyDetailDao.getUserId()).build());
        // 스테이킹 금액
        BigDecimal price = itemBuyDetailDao.getPrice();
        BigDecimal interest = BigDecimal.valueOf( Integer.parseInt(itemDao.getInterest())*0.01 );
        // 스테이킹 이자
        BigDecimal calculateInterest = price.multiply(interest).setScale(3,BigDecimal.ROUND_DOWN);

        // 정산 리스트
        // 추가 입금 금액
        List<BigDecimal> depositList = new ArrayList<>();
        // 출금 신청 금액
        List<BigDecimal> withdrawalList = new ArrayList<>();
        // 입출금 신청 목록
        List<ItemBuySettlementDao> itemBuySettlementDaoList = itemBuySettlementMapper.list(ItemBuySettlementDao.builder().itemUid(itemDao.getUid()).userId(itemBuyDetailDao.getUserId()).build());

        itemBuySettlementDaoList.forEach(itemBuySettlementDao -> {
          // 추가 입금
          if( ItemBuySettlementDao.SettlementType.DEPOSIT.name().equals(itemBuySettlementDao.getType())){
            depositList.add(itemBuySettlementDao.getPrice());

            // 출금 신청
          }else if (ItemBuySettlementDao.SettlementType.WITHDRAWAL.name().equals(itemBuySettlementDao.getType() )){
            // 승인 건만 출금
            if(itemBuySettlementDao.isWithdrawalApprovalFlag()){
              //출금 신청금액
              BigDecimal settlementPrice = itemBuySettlementDao.getPrice().setScale(3,RoundingMode.DOWN);
              withdrawalList.add(settlementPrice);

              WalletTransactionPutRequestModel walletTransactionPutRequestModel =
                WalletTransactionPutRequestModel.builder()
                  .userId(memberWalletDao.getUserId())
                  .tokenId(2L)
                  .coinId(2L)
                  .type("18") // Staking 신청
                  .amount(settlementPrice.doubleValue())
                  .memo("스테이킹 상품 : " + itemDao.getUid() + " 출금")
                  .event(false)
                  .memberUid(memberWalletDao.getMemberUid())
                  .build();
              try{
                memberWalletService.postTransactionsPut(walletTransactionPutRequestModel);
                walletTransactionPutRequestModelList.add(walletTransactionPutRequestModel);
              }catch (Exception e){
                log.error("----------------------------------------");
                log.error(walletTransactionPutRequestModel.getUserId());
                log.error(walletTransactionPutRequestModel.getType());
                log.error("----------------------------------------");
                throw new ServiceException(e.getMessage());
              }



              TransactionHistoryDao transactionHistoryDao = TransactionHistoryDao.builder()
                .price(settlementPrice)
                .unit(ItemBuyInsertRequestModel.PriceUnit.ETH.name())
                .memberUid(memberWalletDao.getMemberUid())
                .transactionKindUid(18L)
                .transactionUid(itemDao.getUid())
                .userId(memberWalletDao.getUserId())
                .build();

              transactionHistoryMapper.insert(transactionHistoryDao);
              itemBuySettlementDao.setWithdrawalCompletedFlag(true);
              itemBuySettlementMapper.updateWithdrawalCompletedFlag(itemBuySettlementDao);


            }
          }
        });
        BigDecimal sumDeposit = depositList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumWithdrawal = withdrawalList.stream().reduce(BigDecimal.ZERO, BigDecimal::subtract);

        // 추천인 수익 mETH 지급
        // 추천인 수익을 얻으려면 진행중인 회차 5ETH 이상 스테이킹 되어야 함
        // 추천인 현재 스테이킹 밸런스 조회
        // 멤버 아이디 기준
        if(!StringUtils.isEmpty(itemBuyDetailDao.getReferrer())){
          MemberWalletDao referrerMemberWalletDao = memberWalletMapper.get(MemberWalletDao.builder().referrerCode(itemBuyDetailDao.getReferrer()).build());
          if( null != referrerMemberWalletDao ){
            // 이더 기준(mETH제외)
            ItemBuyDetailDao stakerItemBuyDetailDao = itemBuyMapper.get(
              ItemBuyListRequestModel.builder()
                .userId(memberWalletDao.getUserId() )
                .itemUid(itemDao.getUid())
                .priceUnit(ItemBuyInsertRequestModel.PriceUnit.ETH)
                .build());
            if( null != stakerItemBuyDetailDao ){
              // 지갑 발급 받지 않은 사용자가 생길 수 있음
              if( !StringUtils.isEmpty( referrerMemberWalletDao.getUserId()) && !StringUtils.isEmpty(referrerMemberWalletDao.getAddress())){
                DateTimeFormatter yymmddFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
                String startAtStr = yymmddFormat.format(itemDao.getStartAt());
                LocalDate startAt = LocalDate.of(itemDao.getStartAt().getYear(),itemDao.getStartAt().getMonthValue(),itemDao.getStartAt().getDayOfMonth());
                ItemBuyDetailDao referrerItemBuyDetailDao = itemBuyMapper.get(
                  ItemBuyListRequestModel.builder()
                    .userId(referrerMemberWalletDao.getUserId() )
                    .priceUnit(ItemBuyInsertRequestModel.PriceUnit.ETH)
                    .startAt(startAt)
                    .processStatus("2").build());
                if( null != referrerItemBuyDetailDao){
                  BigDecimal compareEth = BigDecimal.valueOf(5);
                  // 진행 중인 스테이킹이 5이더 보다 크면 레퍼러 수익 지급
                  if( referrerItemBuyDetailDao.getPrice().compareTo(compareEth) > -1){

                    BigDecimal referrerInterest = BigDecimal.valueOf( Integer.parseInt(itemDao.getReferrerInterest())*0.01 );
                    // 레퍼러 수익 지급
                    BigDecimal referrerCalculateInterest = stakerItemBuyDetailDao.getPrice().multiply(referrerInterest).setScale(3, RoundingMode.DOWN);
                    String memo = year +" "+ itemDao.getRound() +"회차 레퍼럴 유저 수익지급 " + memberWalletDao.getReferrerCode() +" "+memberWalletDao.getUserId() ;
                    WalletTransactionPutRequestModel walletTransactionPutRequestModel =
                      WalletTransactionPutRequestModel.builder()
                        .userId(referrerMemberWalletDao.getUserId())
                        .tokenId(2L)
                        .coinId(2L)
                        .type("23")
                        .amount(referrerCalculateInterest.doubleValue())
                        .memo(memo)
                        .event(false)
                        .memberUid(referrerMemberWalletDao.getMemberUid())
                        .build();

                    try{
                      memberWalletService.postTransactionsPut(walletTransactionPutRequestModel);
                      walletTransactionPutRequestModelList.add(walletTransactionPutRequestModel);
                    }catch (Exception e){
                      log.error("----------------------------------------");
                      log.error(walletTransactionPutRequestModel.getUserId());
                      log.error(walletTransactionPutRequestModel.getType());
                      log.error("----------------------------------------");
                      throw new ServiceException(e.getMessage());


                    }

                    TransactionHistoryDao transactionHistoryDao = TransactionHistoryDao.builder()
                      .price(referrerCalculateInterest)
                      .unit(ItemBuyInsertRequestModel.PriceUnit.ETH.name())
                      .memberUid(referrerMemberWalletDao.getMemberUid())
                      .transactionKindUid(23L)
                      .transactionUid(itemDao.getUid())
                      .userId(referrerMemberWalletDao.getUserId())
                      .build();

                    transactionHistoryMapper.insert(transactionHistoryDao);

//              WalletMEthTransactionPutRequestModel walletMEthTransactionPutRequestModel = WalletMEthTransactionPutRequestModel.builder()
//                .methUseAmount(referrerCalculateInterest)
//                .methType(21L)
//                .memo(memo) // "2023 1회차 레퍼럴 수익 제공"
//                .memberUid(referrerMemberWalletDao.getMemberUid())
//                .walletId(referrerMemberWalletDao.getUid())
//                .itemBuyUid(itemBuyDetailDao.getUid())
//                .itemUid(itemBuyDetailDao.getItemUid())
//                .build();
//              memberWalletService.postMEthTransactionsPut(walletMEthTransactionPutRequestModel);
                  }
                }
              }
            }
          }else{
            log.debug(itemBuyDetailDao.getReferrer());
          }

        }
        // 이자 지급 히스토리
        TransactionHistoryDao transactionHistoryDao = TransactionHistoryDao.builder()
          .price(calculateInterest)
          .unit(ItemBuyInsertRequestModel.PriceUnit.ETH.name())
          .memberUid(memberWalletDao.getMemberUid())
          .transactionKindUid(34L)
          .transactionUid(itemDao.getUid())
          .userId(memberWalletDao.getUserId())
          .build();

        transactionHistoryMapper.insert(transactionHistoryDao);

        // 자동스테이킹 원금 + 이자
        // close_round + 1
        // 자동 스테이킹
        BigDecimal autoStakingPrice = price.add(sumDeposit).add(sumWithdrawal).add(calculateInterest);
        ItemDao autoStakingItemDao = null;
        if( BigDecimal.ZERO.compareTo( (autoStakingPrice)) < 0 ){
          // 일반 상품
          if( itemDao.getItemKind() == 1){
            String selectRound = itemDao.getCloseRound();
            if( itemDao.getCloseRound().equals("36")) {
              autoStakingItemDao = itemMapper.get(ItemListRequestModel.builder().year(nextYear).round("1").build());
            }else{
              int itemYear = itemDao.getEndAt().getYear();
              autoStakingItemDao = itemMapper.get(ItemListRequestModel.builder().year(Integer.toString(itemYear)).round(selectRound).build());
            }
          }else { // 임시 상품
            if( 0 < itemDao.getAutoItemUid() ){
              autoStakingItemDao = itemMapper.get(ItemListRequestModel.builder().uid(itemDao.getAutoItemUid()).build());
            }

          }
          // 기존 구매 스테이킹 자동 진행으로 업데이트
          itemBuyMapper.updateAutoProgressFlag(ItemBuyDao.builder().autoProgressFlag(true).itemUid(itemDao.getUid()).userId(memberWalletDao.getUserId()).build());
          // 자동 스테이킹
          ItemBuyDao itemBuyDao = ItemBuyDao.builder()
            .price(autoStakingPrice)
            .memberUid(memberWalletDao.getMemberUid())
            .marketItemUid(0)
            .priceUnit(ItemBuyInsertRequestModel.PriceUnit.ETH.name())
            .itemUid(autoStakingItemDao.getUid())
            .userId(memberWalletDao.getUserId())
            .autoProgressFlag(false)
            .build();
          itemBuyMapper.insert(itemBuyDao);


          transactionHistoryDao = TransactionHistoryDao.builder()
            .price(autoStakingPrice)
            .unit(ItemBuyInsertRequestModel.PriceUnit.ETH.name())
            .memberUid(itemBuyDao.getMemberUid())
            .transactionKindUid(25L)
            .transactionUid(itemBuyDao.getUid())
            .userId(memberWalletDao.getUserId())
            .build();

          transactionHistoryMapper.insert(transactionHistoryDao);
        }


      });

    }catch (Exception e){
      walletTransactionPutRequestModelList.forEach(walletTransactionPutRequestModel -> {

        try{
          walletTransactionPutRequestModel.setMemo("Error Rollback");
          walletTransactionPutRequestModel.setType("99");
          walletTransactionPutRequestModel.setAmount(walletTransactionPutRequestModel.getAmount()*-1);
          memberWalletService.postTransactionsPut(walletTransactionPutRequestModel);
        }catch (Exception ex){
          throw new ServiceException(ex.getMessage());
        }
      });
    }
  }

}
