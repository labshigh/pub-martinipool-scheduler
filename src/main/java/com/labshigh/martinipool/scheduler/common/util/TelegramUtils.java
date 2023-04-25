package com.labshigh.martinipool.scheduler.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.labshigh.martinipool.core.helper.WebClientHelper;
import com.labshigh.martinipool.core.models.ResponseModel;
import com.labshigh.martinipool.scheduler.common.util.models.TelegramMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Log4j2
@Component
public class TelegramUtils {
    @Value("${telegram.access_token}")
    private String accessToken;
    @Value("${telegram.chat_id}")
    public String chatId;

    /**
     * 텔레그램 메시지 전송(Get 방식 짧은 내용)
     * @param t_message 전달하려는 메시지
     * @return
     */
    public ResponseModel getSend(String t_message) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("[accessToken] : " + accessToken);
        System.out.println("[chatId] : " + chatId);
        String url = "https://api.telegram.org/bot"+accessToken+"/sendMessage?chat_id="+chatId+"&text="+t_message;
        System.out.println("[Telegram Request Url] : " + url);
        ResponseModel responseModel = new ResponseModel();
        try {
            // 요청 URL 세팅
            URI uri = new URI(url);
            // GET 으로 요청
            String sendResult = WebClientHelper.getInstance().get(uri);
            // 반환정보가 JSON 으로 오기에 형변환을 위한 작업
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(sendResult);
            // 반환정보 중 키가 ok 인 값이 true 인 경우 성공
            if(element.getAsJsonObject().get("ok").getAsBoolean()) {
                // 성공한 정보를 센 해준다.
                responseModel.setData(element);
            } else {
                // 실패했을 경우 반환정보를 셋 한다.
                responseModel.setData(element);
                // 상태코드를 반환받은 정보로 셋 한다.
                responseModel.setStatus(element.getAsJsonObject().get("error_code").getAsInt());
                // 실패 메시지 정보를 반환받은 정보로 셋 한다.
                responseModel.setMessage(element.getAsJsonObject().get("description").getAsString());
                responseModel.error.setErrorCode(HttpStatus.BAD_REQUEST.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseModel.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            responseModel.error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseModel.error.setErrorMessage(e.getLocalizedMessage());
        }
        return responseModel;
    }

    /**
     * 텔레그램 메시지 전송(POST 방식 긴 내용)
     * @param telegramMessage 전달하려는 메시지
     * @return
     */
    public ResponseModel postSend(String telegramMessage) throws InterruptedException {
        Thread.sleep(1000);
        log.info("[accessToken] : " + accessToken);
        log.info("[chatId] : " + chatId);
        String url = "https://api.telegram.org/bot"+accessToken+"/sendMessage";
        // 요청 URL 세팅

        log.info("[Telegram Request Url] : " + url);
        ResponseModel responseModel = new ResponseModel();
        TelegramMessage msg = new TelegramMessage();

        int retryCount = 5;
        while(true) {
            --retryCount;
            if (retryCount <= 0) {
                break;
            }
            try {
                URI uri = new URI(url);
                log.info("[Telegram Request t_message] : " + telegramMessage);
                msg.setText(telegramMessage);
                msg.setChat_id(chatId);
                String sendResult = WebClientHelper.getInstance().post(uri, msg);
                responseModel.setData(sendResult);
                return responseModel;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return responseModel;
    }
}
