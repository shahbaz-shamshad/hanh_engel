package hanh_engel.webplatform.service;

import hanh_engel.webplatform.security.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LogoutService {
    private static final Logger logger = LogManager.getLogger(LogoutService.class);

    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    @Autowired
    private JwtUtil jwtUtil;

    public void logout(String token) {
        try {
            logger.debug("Processing logout for token: {}", token);

            if (jwtUtil.isTokenExpired(token)) {
                logger.warn("Logout failed - Token already expired: {}", token);
                return;
            }

            tokenBlacklist.add(token);
            logger.info("Token blacklisted successfully: {}", token);
        } catch (Exception e) {
            logger.error("Logout failed for token: {} - Error: {}", token, e.getMessage());
            throw new RuntimeException("Logout failed", e);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}