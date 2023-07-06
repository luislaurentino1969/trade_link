package com.sp.tradelink.main;

import com.sp.tradelink.services.Test1Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sp.tradelink"})
public class TradeLinkApplication implements CommandLineRunner {

    @Autowired
    private Test1Listener listener1;

    public static void main(String[] args) {
        SpringApplication.run(TradeLinkApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        listener1.publishReservation("Luis 1");
    }
}
