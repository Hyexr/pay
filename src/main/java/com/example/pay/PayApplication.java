package com.example.pay;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan(basePackages = "com.example.pay.dao")
@Slf4j
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
        log.info("启动");
    }

}
