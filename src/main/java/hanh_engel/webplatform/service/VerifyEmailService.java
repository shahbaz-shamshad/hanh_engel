package hanh_engel.webplatform.service;

import hanh_engel.webplatform.entity.User;
import hanh_engel.webplatform.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifyEmailService {

    private static final Logger logger = LogManager.getLogger(VerifyEmailService.class);

    @Autowired
    private UserRepository userRepository;

    public void verifyEmail(String email, String code) {
        try {
            logger.info("Verifying email for: {} with code: {}", email, code);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.warn("Email verification failed - User not found: {}", email);
                        return new RuntimeException("User not found");
                    });

            if (user.isVerified()) {
                logger.warn("Email already verified: {}", email);
                throw new RuntimeException("Email already verified");
            }

            if (!code.equals(user.getVerificationCode())) {
                logger.warn("Email verification failed - Invalid code for email: {}", email);
                throw new RuntimeException("Invalid verification code");
            }

            user.setVerified(true);
            userRepository.save(user);
            logger.info("Email verified successfully for: {}", email);
        } catch (Exception e) {
            logger.error("Email verification failed for: {} - Error: {}", email, e.getMessage());
            throw new RuntimeException("Email verification failed: " + e.getMessage());
        }
    }
}