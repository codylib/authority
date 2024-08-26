package com.example;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TemplateApplicationTests {
    
    @Value("${password.salt}")
    private String salt;
    
    @Test
    void contextLoads() {
        HMac mac = new HMac(HmacAlgorithm.HmacSHA256, salt.getBytes());
        System.out.println(mac.digestBase64("111111", false));
    }
    
}
