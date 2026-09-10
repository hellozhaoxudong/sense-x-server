package com.sense.app.core.oauth.manager;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dypnsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.sense.app.core.exception.BizException;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

/**
 * 短信
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SmsCodeManager {

    @Value("${aliyun.sms.key}")
    private String accessKey;

    @Value("${aliyun.sms.secret}")
    private String accessSecret;

    /**
     * 发送短信
     */
    public String sendSmsCode(String phone){
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder().accessKeyId(accessKey).accessKeySecret(accessSecret).build());

        AsyncClient client = AsyncClient.builder().region("cn-qingdao").credentialsProvider(provider).overrideConfiguration(ClientOverrideConfiguration.create().setEndpointOverride("dypnsapi.aliyuncs.com")).build();

        // 生成4位验证码
        String code = RandomUtil.randomNumbers(4);

        // 构建请求信息
        SendSmsVerifyCodeRequest request = SendSmsVerifyCodeRequest.builder()
                .phoneNumber(phone)
                .signName("速通互联验证服务")   // 签名名称
                .templateCode("100001")      // 短信模板
                .templateParam("{\"code\":\"" + code + "\",\"min\":\"5\"}")
                .build();

        // 发送请求
        CompletableFuture<SendSmsVerifyCodeResponse> response = client.sendSmsVerifyCode(request);

        try{
            SendSmsVerifyCodeResponse resp = response.get();
            if (!"OK".equals(resp.getBody().getCode())){
                throw new BizException(resp.getBody().getMessage());
            }

            return code;
        } catch (BizException e1){
            throw e1;
        } catch (Exception e){
            e.printStackTrace();
            throw new BizException("发送短信验证码失败", e);
        }
    }
}
