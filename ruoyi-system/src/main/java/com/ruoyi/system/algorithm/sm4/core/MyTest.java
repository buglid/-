package com.ruoyi.system.algorithm.sm4.core;

/**
 * 对核心SM4 core，我的测试类。
 */
public class MyTest {

    public static void main(String[] args) {
        testECB();
        testCBC();
    }

    private static void testECB() {
        // 明文
        String plainText = "ECB分组模式";

        // 密钥
        SM4Util sm4 = new SM4Util();
        sm4.secretKey = "Da&^*9wHFOMfs2Y8";// 16位，必须，否则key error!

        // 加密
        String cipherText = sm4.encryptDataByECB(plainText);

        // 解密
        String plainText2 = sm4.decryptDataByECB(cipherText);

        System.out.println("明文: " + plainText);
        System.out.println("加密后密文: " + cipherText);
        System.out.println("解密后明文: " + plainText2);
    }

    private static void testCBC() {
        // 明文
        String plainText = "明文-CBC模式加密";

        // 密钥
        SM4Util sm4 = new SM4Util();
        sm4.secretKey = "#$Lk@Aug22123456";// 16位

        // CBC专有的初始化向量
        sm4.iv = "Saiai-fo@1231234";// 16位

        // 加密
        String cipherText = sm4.encryptData_CBC(plainText);

        // 解密
        String plainText2 = sm4.decryptData_CBC(cipherText);

        System.out.println("明文: " + plainText);
        System.out.println("加密后密文: " + cipherText);
        System.out.println("解密后明文: " + plainText2);

    }
}
