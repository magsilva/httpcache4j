package org.codehaus.httpcache4j.auth.mac;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.base.Charsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * @author Erlend Hamnaberg<erlend@hamnaberg.net>
 */
public enum Algorithm {
    HMAC_SHA_1,
    HMAC_SHA_256;

    public Mac getEncoder(final String key) {
        Mac mac;
        try {
            switch (this) {
                case HMAC_SHA_1:
                    mac = Mac.getInstance("HmacSHA1");
                    mac.init(new SecretKeySpec(key.getBytes(Charsets.UTF_8), "HmacSHA1"));
                    break;
                case HMAC_SHA_256:
                    mac = Mac.getInstance("HmacSHA256");
                    mac.init(new SecretKeySpec(key.getBytes(Charsets.UTF_8), "HmacSHA256"));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown cipher");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        return mac;
    }
    
    public String encode(String key, String value) {
        Mac encoder = getEncoder(key);
        byte[] encoded = encoder.doFinal(value.getBytes(Charsets.UTF_8));
        return Base64.encodeBase64String(encoded).trim();
    }
}
