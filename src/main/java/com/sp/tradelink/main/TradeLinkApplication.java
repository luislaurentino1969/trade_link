package com.sp.tradelink.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sp.tradelink"})
public class TradeLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeLinkApplication.class, args);
    }

}
