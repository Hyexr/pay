package com.example.pay.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.example.pay.common.PayConst;
import com.example.pay.dao.PayInfoMapper;
import com.example.pay.enums.PayPlatformEnum;
import com.example.pay.pojo.PayInfo;
import com.example.pay.service.IPayService;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : pay
 * @Package : com.example.pay.service.Impl
 * @ClassName : PayService.java
 * @createTime : 2023/6/9 10:47
 */
@Slf4j
@Service
public class PayServiceImpl implements IPayService {

    @Autowired
    private BestPayService bestPayService;

    @Autowired
    private PayInfoMapper payInfoMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum) {
        log.info("创建支付订单");

        if(bestPayTypeEnum != BestPayTypeEnum.WXPAY_NATIVE && bestPayTypeEnum != BestPayTypeEnum.ALIPAY_PC){
            throw new RuntimeException("暂不支持的支付类型");
        }
        PayInfo payInfo = new PayInfo(Long.parseLong(orderId), PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
                                        OrderStatusEnum.NOTPAY.name(), amount);
        payInfoMapper.insertSelective(payInfo);

        PayRequest payRequest = new PayRequest();
        payRequest.setOrderName("10649367-支付");
        payRequest.setOrderId(orderId);
        payRequest.setOrderAmount(amount.doubleValue());
        payRequest.setPayTypeEnum(bestPayTypeEnum);


        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("发起支付 response={}", payResponse);

        return payResponse;
    }

    @Override
    public String asyncNotify(String notifyData) {
        //签名校验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("异步通知 response={}", payResponse);
        //金额校验（从数据库查订单）
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));
        if(payInfo == null) {
            //告警
            throw new RuntimeException("通过orderNo查询到的结果是null");
        }
        if(!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())){
            if(payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {
                //告警
                throw new RuntimeException("异步通知中的金额和数据库里的不一致，orderNo=" + payResponse.getOrderId());
            }
            //修改订单支付状态
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());
            payInfo.setUpdateTime(null);
            payInfoMapper.updateByPrimaryKeySelective(payInfo);
        }
        //pay发送MQ消息，mall接收MQ消息
        amqpTemplate.convertAndSend(PayConst.PAY_NOTIFY_QUEUE, JSON.toJSONString(payInfo));

        if(payResponse.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            //告诉微信不要再通知了
            return """
                    <xml>
                      <return_code><![CDATA[SUCCESS]]></return_code>
                      <return_msg><![CDATA[OK]]></return_msg>
                    </xml>""";
        } else if(payResponse.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            //告诉支付宝不要再通知了
            return "success";
        }

        throw new RuntimeException("异步通知中错误的支付平台");
    }

    @Override
    public PayInfo queryByOrderId(String orderId) {
        log.info("查询支付记录");
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(orderId));
        return payInfo;
    }
}
