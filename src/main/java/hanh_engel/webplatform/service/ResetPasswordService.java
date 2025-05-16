package hanh_engel.webplatform.service;

import hanh_engel.webplatform.entity.User;
import hanh_engel.webplatform.repository.UserRepository;
import hanh_engel.webplatform.security.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService {

    private static final Logger logger = LogManager.getLogger(ResetPasswordService.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void resetPassword(String token, String newPassword) {
        try {
            logger.info("Processing password reset for token: {}", token);

            if (!jwtUtil.validateToken(token)) {
                logger.warn("Password reset failed - Invalid or expired token: {}", token);
                throw new RuntimeException("Invalid or expired token");
            }

            String email = jwtUtil.extractEmail(token);
            logger.debug("Extracted email from token: {}", email);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.warn("Password reset failed - User not found for email: {}", email);
                        return new RuntimeException("User not found for email: " + email);
                    });

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            logger.info("Password reset successful for user: {}", email);
        } catch (Exception e) {
            logger.error("Password reset failed for token: {} - Error: {}", token, e.getMessage());
            throw new RuntimeException("Failed to reset password: " + e.getMessage(), e);
        }
    }
}