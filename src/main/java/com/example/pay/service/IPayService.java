package com.example.pay.service;

import com.example.pay.pojo.PayInfo;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : pay
 * @Package : com.example.pay.service
 * @ClassName : IPayService.java
 * @createTime : 2023/6/9 10:46
 */
public interface IPayService {
    PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);

    String asyncNotify(String notifyData);

    PayInfo queryByOrderId(String orderId);
}

