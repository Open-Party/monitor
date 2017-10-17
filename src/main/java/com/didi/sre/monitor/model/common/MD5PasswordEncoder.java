package com.didi.sre.monitor.model.common;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;

/**
 * @author soarpenguin on 17-9-13.
 */
public class MD5PasswordEncoder implements PasswordEncoder {
    private static final Logger logger = LoggerFactory.getLogger(MD5PasswordEncoder.class);

    @Override
    public String encode(CharSequence charSequence) {
        String encPass = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(charSequence.toString().getBytes());
            byte[] b64 = Base64.encodeBase64(digest);
            encPass = new String(b64);
            encPass = encPass.replaceAll("=", "");
        }catch(Exception ex){
            logger.error("An exception trying to encode a password", ex);
        }

        return encPass;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encode(charSequence).equals(s);
    }
}
