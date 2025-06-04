package hanh_engel.webplatform.service;

import hanh_engel.webplatform.entity.User;
import hanh_engel.webplatform.repository.UserRepository;
import hanh_engel.webplatform.security.JwtUtil;
import hanh_engel.webplatform.service.EmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordService {

    private static final Logger logger = LogManager.getLogger(ForgotPasswordService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    public String sendPasswordResetLink(String email) {
        try {
            logger.info("Processing password reset request for email: {}", email);
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                logger.warn("Password reset failed - Email not found: {}", email);
                throw new RuntimeException("Email not found");
            }

            User user = userOptional.get();
            String resetToken = jwtUtil.generateToken(user.getEmail());
            String resetLink = "http://localhost:5173/reset-password?token=" + resetToken;

            emailService.sendEmail(user.getEmail(), "Password Reset",
                    "Click here to reset your password: " + resetLink);

            logger.info("Password reset link sent successfully to email: {}", email);
            return "Password reset link sent to your email";
        } catch (Exception e) {
            logger.error("Failed to send password reset link to email: {} - Error: {}",
                    email, e.getMessage());
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}