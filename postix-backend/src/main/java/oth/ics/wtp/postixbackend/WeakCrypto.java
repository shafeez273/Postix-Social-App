package oth.ics.wtp.postixbackend;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class WeakCrypto {
    private static final String SHA_256 = "SHA-256";
    private static MessageDigest digest;

    public static String base64decode(String authHeader) {
        return new String(Base64.getDecoder().decode(authHeader), UTF_8);
    }

    // Do not use SHA-256 hashing for productive applications! There are stronger encryption algorithms, e.g. BCrypt.
    public static synchronized String hashPassword(String password) {
        try {
            if (digest == null) {
                digest = MessageDigest.getInstance(SHA_256);
            }
            return new String(digest.digest(password.getBytes(UTF_8)), UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

