package com.example.pay.controller;

import com.example.pay.pojo.PayInfo;
import com.example.pay.service.IPayService;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : pay
 * @Package : com.example.pay.controller
 * @ClassName : PatController.java
 * @createTime : 2023/6/9 11:20
 */
//@Controller
@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController {

//    @Autowired
//    private PayService payService;

    @Autowired
    private IPayService payService;

    @Autowired
    private WxPayConfig wxPayConfig;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam("payType")BestPayTypeEnum bestPayTypeEnum
                               ) {
       log.info("创建支付订单");
       PayResponse payResponse = payService.create(orderId, amount, bestPayTypeEnum);
       Map<String, String> map = new HashMap<> ();
       //支付方式不同，渲染就不同，WXPAY_NATIVE使用codeUrl，ALIPAY_PC使用body
        if(bestPayTypeEnum == BestPayTypeEnum.WXPAY_NATIVE)  {
            map.put("codeUrl", payResponse.getCodeUrl());
            map.put("orderId", orderId);
            map.put("returnUrl", wxPayConfig.getReturnUrl());
            return new ModelAndView("createForWxNative", map);
        }
        else if(bestPayTypeEnum == BestPayTypeEnum.ALIPAY_PC) {
            map.put("body", payResponse.getBody());
            return new ModelAndView("createForAlipayPc", map);
        }
//       return new ModelAndView("create", map);
        throw new RuntimeException("暂不支持的支付类型");
    }

    @PostMapping("/notify")
//    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData) {
        return payService.asyncNotify(notifyData);
    }

    @GetMapping("/queryByOrderId")
    public PayInfo queryByOrderId(@RequestParam("orderId") String orderId) {
        return payService.queryByOrderId(orderId);
    }
}
