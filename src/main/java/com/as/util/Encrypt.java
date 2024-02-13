package com.as.util;

/**
 * Created by winbill on 2019/6/22.
 */
public class Encrypt {
    private static String PRIVATE_KEY = "SWB_";
    /**
     * 根据URI, 生成访问token
     * @param uri
     */
    public static String EncryptUri(String uri) {
        return MD5.md5(PRIVATE_KEY + uri);
    }
}
