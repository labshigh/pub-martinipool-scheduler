package com.labshigh.martinipool.scheduler.web3.service;

import com.labshigh.martinipool.core.utils.JsonUtils;
import com.labshigh.martinipool.scheduler.web3.dao.MarketItemDao;
import com.labshigh.martinipool.scheduler.web3.mapper.MarketItemMapper;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;

@Log4j2
@Service
public class Web3Service {

  private final String buyer = "0000000000000000000000000000000000000000000000000000000000000000";
  private final Web3j web3j;
  private final MarketItemMapper marketItemMapper;

  public Web3Service(Web3j web3j, MarketItemMapper marketItemMapper) {
    this.web3j = web3j;
    this.marketItemMapper = marketItemMapper;
  }

  public void transactionReceipt() throws Exception {

    // DB 에서 대기중 가져오기
    List<MarketItemDao> marketItemDaoList = marketItemMapper.gets(
        MarketItemDao.builder()
            .mintingStatus(0)
            .build());

    // web3j
    for (MarketItemDao marketItemDao : marketItemDaoList) {

      String transactionHash = marketItemDao.getTransactionHash();

      log.debug("TransactionHash : {}", transactionHash);

      // transactionHash 를 가지고 receipt 가져오기
      EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(
          transactionHash).send();

      log.debug("Transaction : {}",
          JsonUtils.convertObjectToJsonString(ethGetTransactionReceipt));

      if (ethGetTransactionReceipt.getTransactionReceipt().isPresent()) {
        // 민팅 성공시

        log.debug("Status : {}",
            ethGetTransactionReceipt.getTransactionReceipt().get().isStatusOK());

        // status check
        // status - 0x1 : success
        if (ethGetTransactionReceipt.getTransactionReceipt().get().isStatusOK()) {

          String logData = ethGetTransactionReceipt.getTransactionReceipt().get().getLogs().get(4)
              .getData();

          log.debug("Data : {}", logData);

          String code = logData;

          if (code.length() != 514) {
            String codeFront = code.substring(0, 130);
            String codeBack = code.substring(130);
            code = codeFront + buyer + codeBack;
          }

          log.debug("Code : {}", code);

          Function function = new Function("listNFTExternal",
              Arrays.asList(),
              Arrays.asList(new TypeReference<Address>() {
              }, new TypeReference<Address>() {
              }, new TypeReference<Address>() {
              }, new TypeReference<Bool>() {
              }, new TypeReference<Uint256>() {
              }, new TypeReference<Uint256>() {
              }, new TypeReference<Uint256>() {
              }, new TypeReference<Uint256>() {
              }));

          List<Type> type = FunctionReturnDecoder.decode(code, function.getOutputParameters());

          log.debug("nftId : {}", type.get(4).getValue());
          log.debug("saleId : {}", type.get(5).getValue());

          marketItemMapper.update(MarketItemDao.builder()
              .uid(marketItemDao.getUid())
              .nftId(type.get(4).getValue().toString())
              .sellId(type.get(5).getValue().toString())
              .mintingStatus(1)
              .build());
        } else {

          // 민팅 실패시

          marketItemMapper.update(MarketItemDao.builder()
              .uid(marketItemDao.getUid())
              .mintingStatus(2)
              .build());
        }
      } else {

        log.debug("Transaction Complete Not Yet : {}", transactionHash);
      }
    }
  }

  public static String _convertStringToHex(String str) {

    // display in uppercase
    //char[] chars = Hex.encodeHex(str.getBytes(StandardCharsets.UTF_8), false);

    // display in lowercase, default
    char[] chars = Hex.encodeHex(str.getBytes(StandardCharsets.UTF_8));

    return String.valueOf(chars);
  }
}
