package com.ruoyi.system.algorithm.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
//@Data
@ConfigurationProperties(prefix = "sm4config")
public class Sm4Config {

    //密钥
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String toString() {
        return "Sm4Config{" +
                "secretKey='" + secretKey + '\'' +
                '}';
    }
}
