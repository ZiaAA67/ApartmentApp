package com.mntn.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HmacUtils {

    public static String hmacSHA256(String data, String secretKey) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            byte[] keyBytes = secretKey.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
            hmac.init(secretKeySpec);
            byte[] hashBytes = hmac.doFinal(data.getBytes());
            return bytesToHex(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error creating HMAC SHA256 signature", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
