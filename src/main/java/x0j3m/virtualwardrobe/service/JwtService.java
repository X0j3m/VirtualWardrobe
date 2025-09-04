package x0j3m.virtualwardrobe.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private final int tokenExpirationTime;

    public JwtService(@Value("${security.jwt.token.expirationTime}") int tokenExpirationTimeInMinutes) {
        this.tokenExpirationTime = tokenExpirationTimeInMinutes;
    }

    public String generateToken(String subject) {
        try {
            Map<String, Object> claims = new HashMap<>();

            return Jwts
                    .builder()
                    .claims()
                    .add(claims)
                    .subject(subject)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + minutesToMillis(tokenExpirationTime)))
                    .and()
                    .signWith(generateKey())
                    .compact();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private Key generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        String secretKey = Arrays.toString(keyGenerator.generateKey().getEncoded());
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private long minutesToMillis(int minutes) {
        return 1000L * 60 * minutes;
    }
}
