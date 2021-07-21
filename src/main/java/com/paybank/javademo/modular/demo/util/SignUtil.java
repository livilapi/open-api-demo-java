package com.paybank.javademo.modular.demo.util;

import java.util.Map;

public class SignUtil {

    private static final String SIGN = "sign";

    private static final String SECRET = "AppSecret";

    public static String getSign(Map<String, Object> params, String secret) {
        StringBuffer sb = new StringBuffer();
        params.entrySet().stream().sorted(Map.Entry.<String, Object>comparingByKey()).forEachOrdered(x -> {
            if (!SIGN.equals(x.getKey()) && null != x.getValue()) {
                sb.append(x.getKey().toUpperCase()).append("=").append(x.getValue()).append("&");
            }
        });
        sb.append(SECRET.toUpperCase()).append("=").append(secret);
        return MD5Util.encrypt(sb.toString());
    }
}
