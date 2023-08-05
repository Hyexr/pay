package com.example.pay.service.Impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : pay
 * @Package : com.example.pay.service.Impl
 * @ClassName : com.example.pay.service.Impl.PayService.java
 * @createTime : 2023/6/9 10:55
 */
@SpringBootTest
class PayServiceTest {
    @Autowired
    private PayServiceImpl payService;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void create() {
        payService.create("9671532651651", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);
    }

    @Test
    public void sendMQMsg() {
        amqpTemplate.convertAndSend("payNotify", "hello");
    }
}