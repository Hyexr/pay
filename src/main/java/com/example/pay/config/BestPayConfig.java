package com.example.pay.config;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : pay
 * @Package : com.example.pay.config
 * @ClassName : BestPayConfig.java
 * @createTime : 2023/6/10 15:44
 */
@Component
//@Configuration
public class BestPayConfig {
    @Autowired
    private WxAccountConfig wxAccountConfig;
    @Autowired
    private AliAccountConfig aliAccountConfig;

    @Bean
    public BestPayService bestPayService(WxPayConfig wxPayConfig) {

        AliPayConfig aliPayConfig = new AliPayConfig();
        aliPayConfig.setAppId(aliAccountConfig.getAppId());
        aliPayConfig.setPrivateKey(aliAccountConfig.getPrivateKey());
        aliPayConfig.setAliPayPublicKey(aliAccountConfig.getPublicKey());
        aliPayConfig.setNotifyUrl(aliAccountConfig.getNotifyUrl());
        aliPayConfig.setReturnUrl(aliAccountConfig.getReturnUrl());

        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);
        bestPayService.setAliPayConfig(aliPayConfig);
        return bestPayService;
    }

    @Bean
    public WxPayConfig wxPayConfig(){
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wxAccountConfig.getAppId());
        wxPayConfig.setMchId(wxAccountConfig.getMchId());
        wxPayConfig.setMchKey(wxAccountConfig.getMchKey());
        wxPayConfig.setNotifyUrl(wxAccountConfig.getNotifyUrl());
        wxPayConfig.setReturnUrl(wxAccountConfig.getReturnUrl());

        return wxPayConfig;
    }
}
