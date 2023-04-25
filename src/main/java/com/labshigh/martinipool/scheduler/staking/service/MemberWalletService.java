package com.labshigh.martinipool.scheduler.staking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.labshigh.martinipool.core.helper.WebClientHelper;
import com.labshigh.martinipool.core.inherits.AbstractRestService;
import com.labshigh.martinipool.core.utils.exceptions.ServiceException;
import com.labshigh.martinipool.scheduler.common.Constants;
import com.labshigh.martinipool.scheduler.staking.dao.MemberMEthDao;
import com.labshigh.martinipool.scheduler.staking.dao.MemberWalletDao;
import com.labshigh.martinipool.scheduler.staking.dao.WalletTransactionDao;
import com.labshigh.martinipool.scheduler.staking.mapper.MemberMEthMapper;
import com.labshigh.martinipool.scheduler.staking.mapper.MemberWalletMapper;
import com.labshigh.martinipool.scheduler.staking.model.request.WalletBalanceRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.request.WalletMEthTransactionPutRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.request.WalletTransactionPutRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.request.WalletTransactionRequestModel;
import com.labshigh.martinipool.scheduler.staking.model.response.WalletBalanceResponseModel;
import com.labshigh.martinipool.scheduler.staking.model.response.WalletTransactionResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberWalletService extends AbstractRestService {

  private final MemberWalletMapper memberWalletMapper;
  private final MemberMEthMapper memberMEthMapper;

  private final ObjectMapper objectMapper;


  @Value("${martinipool.wallet.api}")
  private String walletApi;

  public MemberWalletDao getMemberWallet(Long memberUid,Long walletId) throws Exception {

    MemberWalletDao memberWalletDao = MemberWalletDao.builder()
      .memberUid(memberUid)
      .uid(walletId)
      .build();
    memberWalletDao = memberWalletMapper.get(memberWalletDao);



    return memberWalletDao;
  }


  @Transactional
  public WalletTransactionResponseModel postTransactionsPut(WalletTransactionPutRequestModel requestModel) throws ServiceException {

    WalletTransactionResponseModel walletTransactionResponseModel = null;

    String responseModel = null;

    if( requestModel.getAmount() < 0 ){
      WalletBalanceRequestModel walletBalanceRequestModel = WalletBalanceRequestModel.builder()
        .memberId(requestModel.getMemberUid())
        .userId(requestModel.getUserId())
        .tokenId(2L)
        .build();
      WalletBalanceResponseModel walletBalanceResponseModel=  this.getBalance(walletBalanceRequestModel);
      if( requestModel.getAmount() + walletBalanceResponseModel.getBalance() < 0 ){
        throw new ServiceException(Constants.MSG_WALLET_INSUFFICIENT_BALANCE);
      }
    }

    URI uri = makeUri(walletApi, "/tokens" +"/put");
    responseModel = WebClientHelper.getInstance().post(uri, requestModel, String.class);
    try {

      walletTransactionResponseModel = objectMapper.readValue(responseModel, WalletTransactionResponseModel.class);
      // 내부 지갑 balance update
      memberWalletMapper.updateWalletBalance(MemberWalletDao.builder()
        .userId(walletTransactionResponseModel.getUserId())
        .balance(BigDecimal.valueOf(walletTransactionResponseModel.getBalance()).setScale(3, RoundingMode.DOWN))
        .build());

      memberWalletMapper.insertWalletTransaction(
        WalletTransactionDao.builder()
          .transactionId(walletTransactionResponseModel.getTransactionId())
          .userId(walletTransactionResponseModel.getUserId())
          .tokenId(walletTransactionResponseModel.getTokenId())
          .status(walletTransactionResponseModel.getStatus())
          .amount(walletTransactionResponseModel.getAmount())
          .fee(walletTransactionResponseModel.getFee())
          .balance(walletTransactionResponseModel.getBalance())
          .txHash(walletTransactionResponseModel.getTxHash())
          .memo(walletTransactionResponseModel.getMemo())
          .toAddress(walletTransactionResponseModel.getToAddress())
          .toUserId(walletTransactionResponseModel.getToUserId())
          .regDate(walletTransactionResponseModel.getRegDate())
          .type(walletTransactionResponseModel.getType())
          .refTransactionId(walletTransactionResponseModel.getRefTransactionId())
          .fromAddress(walletTransactionResponseModel.getFromAddress())
          .feeSymbol(walletTransactionResponseModel.getFeeSymbol())
          .feeTokenId(walletTransactionResponseModel.getFeeTokenId())
          .priceKrw(walletTransactionResponseModel.getPriceKrw())
          .priceUsd(walletTransactionResponseModel.getPriceUsd())
          .innerTx(walletTransactionResponseModel.getInnerTx())
          .memberUid(requestModel.getMemberUid())
          .build());
    }catch (Exception e) {
      throw new ServiceException(e.getMessage());
    }


    return walletTransactionResponseModel;
  }

  public WalletBalanceResponseModel getBalance(WalletBalanceRequestModel requestModel) {

    WalletBalanceResponseModel walletBalanceResponseModel = null;
    // 월렛발급
    String responseModel = null;
    try {

      URI uri = makeUri(walletApi, "/users/"+requestModel.getUserId()+"/tokens/"+requestModel.getTokenId()+"/balance");
      responseModel = WebClientHelper.getInstance().get(uri);
      walletBalanceResponseModel = objectMapper.readValue(responseModel,WalletBalanceResponseModel.class);

    } catch (Exception e) {
      throw new ServiceException(e.getMessage(), e.hashCode());
    }

    return walletBalanceResponseModel;
  }

  @Transactional
  public WalletTransactionResponseModel postTransactions(WalletTransactionRequestModel requestModel) {

    WalletTransactionResponseModel walletTransactionResponseModel = null;
    // 월렛발급
    String responseModel = null;
    try {

      URI uri = makeUri(walletApi, "/tokens/transactions");
      responseModel = WebClientHelper.getInstance().post(uri,requestModel, String.class);
      walletTransactionResponseModel = objectMapper.readValue(responseModel, WalletTransactionResponseModel.class);

      memberWalletMapper.insertWalletTransaction(
        WalletTransactionDao.builder()
          .transactionId(walletTransactionResponseModel.getTransactionId())
          .userId(walletTransactionResponseModel.getUserId())
          .tokenId(walletTransactionResponseModel.getTokenId())
          .status(walletTransactionResponseModel.getStatus())
          .amount(walletTransactionResponseModel.getAmount())
          .fee(walletTransactionResponseModel.getFee())
          .balance(walletTransactionResponseModel.getBalance())
          .txHash(walletTransactionResponseModel.getTxHash())
          .memo(walletTransactionResponseModel.getMemo())
          .toAddress(walletTransactionResponseModel.getToAddress())
          .toUserId(walletTransactionResponseModel.getToUserId())
          .regDate(walletTransactionResponseModel.getRegDate())
          .type(walletTransactionResponseModel.getType())
          .refTransactionId(walletTransactionResponseModel.getRefTransactionId())
          .fromAddress(walletTransactionResponseModel.getFromAddress())
          .feeSymbol(walletTransactionResponseModel.getFeeSymbol())
          .feeTokenId(walletTransactionResponseModel.getFeeTokenId())
          .priceKrw(walletTransactionResponseModel.getPriceKrw())
          .priceUsd(walletTransactionResponseModel.getPriceUsd())
          .innerTx(walletTransactionResponseModel.getInnerTx())
          .memberUid(requestModel.getMemberUid())
          .build());
    } catch (Exception e) {
      throw new ServiceException(e.getMessage(), e.hashCode());
    }

    return walletTransactionResponseModel;
  }

  @Transactional
  public WalletTransactionResponseModel postTransactionsConfirm(long transactionId) {

    WalletTransactionResponseModel walletTransactionResponseModel = null;
    // 월렛발급
    String responseModel = null;
    try {

      URI uri = makeUri(walletApi, walletApi +"/transactions/confirm");
      responseModel = WebClientHelper.getInstance().post(uri,null, String.class);
      walletTransactionResponseModel = objectMapper.readValue(responseModel, WalletTransactionResponseModel.class);
      memberWalletMapper.insertWalletTransaction(
        WalletTransactionDao.builder()
          .transactionId(walletTransactionResponseModel.getTransactionId())
          .userId(walletTransactionResponseModel.getUserId())
          .tokenId(walletTransactionResponseModel.getTokenId())
          .status(walletTransactionResponseModel.getStatus())
          .amount(walletTransactionResponseModel.getAmount())
          .fee(walletTransactionResponseModel.getFee())
          .balance(walletTransactionResponseModel.getBalance())
          .txHash(walletTransactionResponseModel.getTxHash())
          .memo(walletTransactionResponseModel.getMemo())
          .toAddress(walletTransactionResponseModel.getToAddress())
          .toUserId(walletTransactionResponseModel.getToUserId())
          .regDate(walletTransactionResponseModel.getRegDate())
          .type(walletTransactionResponseModel.getType())
          .refTransactionId(walletTransactionResponseModel.getRefTransactionId())
          .fromAddress(walletTransactionResponseModel.getFromAddress())
          .feeSymbol(walletTransactionResponseModel.getFeeSymbol())
          .feeTokenId(walletTransactionResponseModel.getFeeTokenId())
          .priceKrw(walletTransactionResponseModel.getPriceKrw())
          .priceUsd(walletTransactionResponseModel.getPriceUsd())
          .innerTx(walletTransactionResponseModel.getInnerTx())
//          .memberUid(requestModel.getMemberUid())
          .build());
    } catch (Exception e) {
      throw new ServiceException(e.getMessage(), e.hashCode());
    }

    return walletTransactionResponseModel;
  }

  public WalletBalanceResponseModel getMasterBalance(){

    WalletBalanceResponseModel walletBalanceResponseModel = null;
    // 월렛발급
    String responseModel = null;
    try {

      URI uri = makeUri(walletApi, "/tokens/2/master");
      responseModel = WebClientHelper.getInstance().get(uri);
      walletBalanceResponseModel = objectMapper.readValue(responseModel,
        WalletBalanceResponseModel.class);

    } catch (Exception e) {
      throw new ServiceException(e.getMessage(), e.hashCode());
    }

    return walletBalanceResponseModel;
  }
}
