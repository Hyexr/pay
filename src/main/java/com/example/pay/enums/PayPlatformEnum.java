package com.example.pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : pay
 * @Package : com.example.pay.enums
 * @ClassName : PayPlatformEnum.java
 * @createTime : 2023/6/13 10:50
 */
@Getter
public enum PayPlatformEnum {
    ALIPAY(1),

    WX(2),
    ;
    final Integer code;

    PayPlatformEnum(Integer code) {
        this.code = code;
    }
    public static PayPlatformEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum) {
//       if(bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.ALIPAY.name())){
//              return ALIPAY;
//       } else if (bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.WX.name())){
//           return WX;
//       } else {
//           throw new RuntimeException("错误的支付平台");
//       }
        for(PayPlatformEnum payPlatformEnum : PayPlatformEnum.values()){
            if(bestPayTypeEnum.getPlatform().name().equals(payPlatformEnum.name())){
                return payPlatformEnum;
            }
        }
        throw new RuntimeException("错误的支付平台");
    }
}
