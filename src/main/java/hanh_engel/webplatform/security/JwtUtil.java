package hanh_engel.webplatform.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtUtil {

    private static final Logger logger = LogManager.getLogger(JwtUtil.class);


    private static final int MAX_RETAINED_KEYS = 2;

    @Value("${jwt.secret}")
    private String baseSecret;

    @Value("${jwt.expiration.ms:3600000}")
    private long expirationTimeMs;


    private final Map<String, Key> activeKeys = new ConcurrentHashMap<>();
    private String currentKeyId;
    private Key currentKey;

    @PostConstruct
    public void init() {
        validateSecretStrength(baseSecret);
        rotateKey(baseSecret);
    }


    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMs))
//                .setHeaderParam("kid", currentKeyId)
                .signWith(currentKey)
                .compact();
    }

    public String extractEmail(String token) {

        return getClaimsFromToken(token).getSubject();
    }

    public boolean isTokenExpired(String token) {

        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            logger.debug("Validating token: {}", token);
            getClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            logger.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(currentKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public synchronized void rotateKey(String newSecret) {
        validateSecretStrength(newSecret);

        String newKeyId = UUID.randomUUID().toString();
        Key newKey = Keys.hmacShaKeyFor(newSecret.getBytes(StandardCharsets.UTF_8));

        activeKeys.put(newKeyId, newKey);
        this.currentKeyId = newKeyId;
        this.currentKey = newKey;
        logger.info("Key rotated. Active keys: {}", activeKeys.keySet());

        if (activeKeys.size() > MAX_RETAINED_KEYS) {
            Iterator<String> iterator = activeKeys.keySet().iterator();
            while (activeKeys.size() > MAX_RETAINED_KEYS) {
                iterator.next();
                iterator.remove();
            }
        }

        logger.info("Key rotated. Active keys: {}", activeKeys.keySet());
    }



    private Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                    @Override
                    public Key resolveSigningKey(JwsHeader header, Claims claims) {
                        String kid = header.getKeyId();
                        Key key = activeKeys.get(kid);
                        if (key == null) {
                            throw new UnsupportedJwtException("Key ID not found: " + kid);
                        }
                        return key;
                    }
                })
                .build()
                .parseClaimsJws(token);
    }

    private void validateSecretStrength(String secret) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException(
                    "JWT secret must be ≥32 characters. Current length: " +
                            (secret != null ? secret.length() : "null")
            );
        }
    }

}