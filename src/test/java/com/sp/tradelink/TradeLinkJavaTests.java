package com.sp.tradelink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

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

    @Test
    public void updateData() throws JsonProcessingException {

        JsonNode data = new ObjectMapper().readValue("{\"data\": {\"id\":\"123456\", \"disable\":null}}", JsonNode.class);
        AtomicReference<String> where = new AtomicReference<>("");
        AtomicReference<String> set = new AtomicReference<>("SET ");

        data.get("data").fields().forEachRemaining(new Consumer<Map.Entry<String, JsonNode>>() {
            @Override
            public void accept(Map.Entry<String, JsonNode> stringJsonNodeEntry) {
                if (stringJsonNodeEntry.getKey().equals("id")) {
                    where.set(stringJsonNodeEntry.getKey().concat("=").concat(stringJsonNodeEntry.getValue().toString()));
                } else {
                    set.set(set.get().concat(stringJsonNodeEntry.getKey().concat("=")
                                     .concat(stringJsonNodeEntry.getValue().toString())
                                     .concat(data.fields().hasNext() ? "," : "")));
                }
            }
        });
        System.out.println("UPDATE tb_whatever ".concat(set.get().substring(0, set.get().lastIndexOf(","))
                .concat(" WHERE ").concat(where.get())));
        assertTrue(!where.get().isEmpty() && set.get().length()>4);
    }
}
