package com.ruoyi.system.algorithm.sm4;


import com.ruoyi.system.algorithm.sm4.core.SM4Util;
import org.springframework.stereotype.Service;

/**
 * SM4提供给外部调用的类。
 *
 * @author Danni
 * @since 2018.11.23
 */


@Service
public class SM4 {


     public String getKey(){
         String serectKey="Da&^*9wHFOMfs2Y8";
         return serectKey;

     }

    /**
     * 加密。默认为ECB分组模式。
     *
     * @param plainText 明文
     * @return 加密后密文
     */
    public  String encrypt(String plainText) {
        SM4Util sm4 = new SM4Util();
        sm4.secretKey = getKey();// 16位
        // 加密
        String cipherText = sm4.encryptDataByECB(plainText);
        return cipherText;

    }

    /**
     * 解密。默认为ECB分组模式。
     *
     * @param cipherText 密文

     * @return 解密后明文
     */
    public  String decrypt(String cipherText) {
        SM4Util sm4 = new SM4Util();
        sm4.secretKey = getKey();// 16位

        // 解密
        String plainText = sm4.decryptDataByECB(cipherText);
        return plainText;

    }
}


