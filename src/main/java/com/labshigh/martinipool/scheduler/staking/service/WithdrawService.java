package com.labshigh.martinipool.scheduler.staking.service;

import com.labshigh.martinipool.core.inherits.AbstractRestService;
import com.labshigh.martinipool.core.models.ResponseModel;
import com.labshigh.martinipool.core.utils.StringUtils;
import com.labshigh.martinipool.scheduler.common.util.TelegramUtils;
import com.labshigh.martinipool.scheduler.staking.dao.MemberDao;
import com.labshigh.martinipool.scheduler.staking.dao.TransactionHistoryDao;
import com.labshigh.martinipool.scheduler.staking.dao.WithdrawDao;
import com.labshigh.martinipool.scheduler.staking.mapper.*;
import com.labshigh.martinipool.scheduler.staking.model.request.ItemBuyInsertRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.request.WalletTransactionPutRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.request.WalletTransactionRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.response.WalletBalanceResponseModel;
import com.labshigh.martinipool.scheduler.staking.model.response.WalletTransactionResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class WithdrawService extends AbstractRestService {
  private final WithdrawMapper withdrawMapper;
  private final MemberWalletService memberWalletService;
  private final TransactionHistoryMapper transactionHistoryMapper;
  private final TelegramUtils telegramUtils;
  public void walletWithdraw() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // 스테이킹 상품 조회
    List<WithdrawDao> withdrawDaoList = withdrawMapper.list();

    String marsterAddress = "0x4e463ed5bfa6b075c77c9aa5b37038a0f58f8ed3";
    withdrawDaoList.forEach(withdrawDao -> {
      // 어드민 마스터 계좌 출금
      if( withdrawDao.isManualFlag()){
        if(marsterAddress.equals(withdrawDao.getFromWallet())){
          String walletName = null;
          if( withdrawDao.isInternalFlag() ){
            walletName = "내부지갑";
            MemberDao memberDao =withdrawMapper.getWalletMember(MemberDao.builder()
              .address(withdrawDao.getToWallet())
              .build());
            if( null != memberDao){
              WalletTransactionPutRequestModel walletTransactionPutRequestModel =
                WalletTransactionPutRequestModel.builder()
                  .userId(withdrawDao.getToUserId())
                  .tokenId(2L)
                  .coinId(2L)
                  .type("15")
                  .amount(withdrawDao.getRequestQuantity().doubleValue())
                  .memo("마스터 계좌에서 입금")
                  .event(false)
                  .memberUid(memberDao.getUid())
                  .build();
              memberWalletService.postTransactionsPut(walletTransactionPutRequestModel);

              withdrawMapper.updateScheduleStatus(WithdrawDao.builder()
                .uid(withdrawDao.getUid())
                .scheduleStatus(true)
                .build());
              TransactionHistoryDao transactionHistoryDao = TransactionHistoryDao.builder()
                .price(withdrawDao.getRequestQuantity())
                .unit(ItemBuyInsertRequestModel.PriceUnit.ETH.name())
                .memberUid(memberDao.getUid())
                .transactionKindUid(15L)
                .transactionUid(withdrawDao.getUid())
                .userId(withdrawDao.getToUserId())
                .build();
              transactionHistoryMapper.insert(transactionHistoryDao);

              walletName = "내부지갑";

                String msg =  "관리자님이 " + walletName +": "+ withdrawDao.getToWallet() + "\n"
                  + memberDao.getEmail() ;
                if( !StringUtils.isEmpty(memberDao.getNickname()) ){
                  msg = msg + "("+memberDao.getNickname() +")";
                }
                msg = msg + " "+ withdrawDao.getRequestQuantity().toString()+" ETH ("+now.format(formatter).toString() +") 입금";
                this.sendTelegram(msg);
            }

          }else{
            WithdrawDao masterWithdraw = WithdrawDao.builder()
              .toWallet(withdrawDao.getToWallet())
              .fromWallet(withdrawDao.getFromWallet())
              .requestQuantity(withdrawDao.getRequestQuantity())
              .build();


            withdrawMapper.insert(masterWithdraw);
            withdrawMapper.updateScheduleStatus(WithdrawDao.builder()
              .uid(withdrawDao.getUid())
              .scheduleStatus(true)
              .build());
            walletName = "외부지갑";
            String msg = "메인지갑 에서 " + walletName +": "+ withdrawDao.getToWallet() + "\n"
              + withdrawDao.getRequestQuantity().toString()+" ETH ("+now.format(formatter).toString() +") 출금";
            this.sendTelegram(msg);

          }



       }
      }else if(withdrawDao.isInternalFlag()) {

        WalletTransactionPutRequestModel walletTransactionPutRequestModel =
          WalletTransactionPutRequestModel.builder()
            .userId(withdrawDao.getUserId())
            .tokenId(2L)
            .coinId(2L)
            .type("19")
            .amount(withdrawDao.getRequestQuantity().doubleValue())
            .memo("사용자 출금 승인")
            .event(false)
            .memberUid(withdrawDao.getMemberUid())
            .build();
        memberWalletService.postTransactionsPut(walletTransactionPutRequestModel);

        // 내부계좌 출금
        WalletTransactionResponseModel walletTransactionResponseModel  = memberWalletService.postTransactions(
          WalletTransactionRequestModel.builder()
            .coinId(2l)
            .tokenId(2l)
            .memo("내부 계좌 송금")
            .userId(withdrawDao.getUserId())
            .toAddress(withdrawDao.getToWallet())
            .toUserId(withdrawDao.getToUserId())
            .receiveType("19")
            .amount(withdrawDao.getRequestQuantity().doubleValue())
            .memberUid(withdrawDao.getMemberUid())
            .build());

        TransactionHistoryDao transactionHistoryDao = TransactionHistoryDao.builder()
          .price(withdrawDao.getRequestQuantity())
          .unit(ItemBuyInsertRequestModel.PriceUnit.ETH.name())
          .memberUid(withdrawDao.getMemberUid())
          .transactionKindUid(19L)
          .transactionUid(withdrawDao.getUid())
          .userId(withdrawDao.getUserId())
          .build();

        transactionHistoryMapper.insert(transactionHistoryDao);

        withdrawMapper.updateScheduleStatus(WithdrawDao.builder()
          .uid(withdrawDao.getUid())
          .scheduleStatus(true)
          .build());
      }

      else if( !withdrawDao.isInternalFlag()){
        // 외부 계좌 출금
//        memberWalletService.postTransactionsConfirm(withdrawDao.getTransactionId());
        WalletTransactionPutRequestModel walletTransactionPutRequestModel =
          WalletTransactionPutRequestModel.builder()
            .userId(withdrawDao.getUserId())
            .tokenId(2L)
            .coinId(2L)
            .type("14")
            .amount(withdrawDao.getRequestQuantity().doubleValue())
            .memo("사용자 출금 승인")
            .event(false)
            .memberUid(withdrawDao.getMemberUid())
            .build();
        memberWalletService.postTransactionsPut(walletTransactionPutRequestModel);

        WalletTransactionResponseModel walletTransactionResponseModel = memberWalletService.postTransactions(
          WalletTransactionRequestModel.builder()
            .coinId(2l)
            .tokenId(2l)
            .memo("외부 계좌 송금")
            .userId(withdrawDao.getUserId())
            .toAddress(withdrawDao.getToWallet())
            .toUserId(withdrawDao.getToUserId())
            .amount(withdrawDao.getRequestQuantity().doubleValue())
            .memberUid(withdrawDao.getMemberUid())
            .receiveType("14")
            .build());

        withdrawDao.setTxHash(walletTransactionResponseModel.getTxHash());
        withdrawDao.setTransactionId(walletTransactionResponseModel.getTransactionId());

        withdrawMapper.updateTxHash(WithdrawDao.builder()
            .uid(withdrawDao.getUid())
            .txHash(walletTransactionResponseModel.getTxHash())
            .transactionId(walletTransactionResponseModel.getTransactionId())
          .build());
        TransactionHistoryDao transactionHistoryDao = TransactionHistoryDao.builder()
          .price(withdrawDao.getRequestQuantity())
          .unit(ItemBuyInsertRequestModel.PriceUnit.ETH.name())
          .memberUid(withdrawDao.getMemberUid())
          .transactionKindUid(14L)
          .txHash(walletTransactionResponseModel.getTxHash())
          .transactionUid(withdrawDao.getUid())
          .userId(withdrawDao.getUserId())
          .build();

        transactionHistoryMapper.insert(transactionHistoryDao);

        withdrawMapper.updateScheduleStatus(WithdrawDao.builder()
          .uid(withdrawDao.getUid())
          .scheduleStatus(true)
          .build());
        MemberDao memberDao = withdrawMapper.getMember(MemberDao.builder()
          .uid(withdrawDao.getMemberUid()).build());
        if( null != memberDao){
          String msg = memberDao.getEmail() ;
          if( !StringUtils.isEmpty(memberDao.getNickname()) ){
            msg = msg + "("+memberDao.getNickname() +")";
          }
          msg = msg + " "+ withdrawDao.getRequestQuantity().toString()+" ETH ("+now.format(formatter).toString() +") 출금";
          this.sendTelegram(msg);
        }
      }
    });
  }

  public void masterWalletBalance() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // 스테이킹 상품 조회
    WalletBalanceResponseModel walletBalanceResponseModel = memberWalletService.getMasterBalance();
    if( walletBalanceResponseModel.getBalance() > 5 ) {
      String msg = "메인지갑 수량 : [ " + walletBalanceResponseModel.getBalance() + " ]ETH" + "\n";
      msg = msg + "(" + now.format(formatter) + ")";

      this.sendTelegram(msg);
    }

  }
  public ResponseModel sendTelegram(String msg) {
    ResponseModel responseModel = null;
    try {
      responseModel = telegramUtils.postSend(msg);

    } catch (InterruptedException e) {
      log.error(e.getMessage());
    }
    return responseModel;
  }
}
