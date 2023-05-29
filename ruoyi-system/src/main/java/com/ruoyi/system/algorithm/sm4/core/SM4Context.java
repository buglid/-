package com.ruoyi.system.algorithm.sm4.core;

/**
 * SM4上下文。
 */
public class SM4Context {
    public int mode;// 加密还是解密

    public long[] sk;// 密钥

    public boolean isPadding;// 是否填充

    public SM4Context()
    {
        this.mode = 1;
        this.isPadding = true;
        this.sk = new long[32];
    }
}

