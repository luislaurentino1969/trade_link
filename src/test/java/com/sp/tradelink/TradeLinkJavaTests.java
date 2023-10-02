package com.sp.tradelink;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.Assert.assertTrue;

public class TradeLinkJavaTests {
    @Test
    public void checkPassword() throws NoSuchAlgorithmException {

        byte[] plainTextBytes = ("6wxjtcQM+KsvHi3xCQW508XuABLacjWH".concat("TradeLink-dev")).getBytes(StandardCharsets.UTF_8);
        byte[] hashBytes = MessageDigest.getInstance("SHA-512").digest(plainTextBytes);

        String passwordTest = Base64.getEncoder().encodeToString(hashBytes);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String result = encoder.encode(passwordTest);

        System.out.println(passwordTest);
        System.out.println(result);
        assertTrue(encoder.matches(passwordTest, result));
    }
}
