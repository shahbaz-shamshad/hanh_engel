package hanh_engel.webplatform.service;

import hanh_engel.webplatform.dto.RegisterRequest;
import hanh_engel.webplatform.dto.UserResponse;
import hanh_engel.webplatform.entity.User;
import hanh_engel.webplatform.entity.Role;
import hanh_engel.webplatform.repository.UserRepository;
import hanh_engel.webplatform.security.JwtUtil;
import hanh_engel.webplatform.util.VerificationCodeGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LogManager.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    public String authenticateUser(String email, String password) {
        try {
            logger.debug("Authenticating user with email: {}", email);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.warn("Authentication failed - User not found: {}", email);
                        return new RuntimeException("User not found");
                    });

            if (!passwordEncoder.matches(password, user.getPassword())) {
                logger.warn("Authentication failed - Invalid password for email: {}", email);
                throw new RuntimeException("Invalid email or password");
            }

            if (!user.isVerified()) {
                logger.warn("Authentication failed - Email not verified: {}", email);
                throw new RuntimeException("Email not verified");
            }

            String token = jwtUtil.generateToken(user.getEmail());
            logger.info("Authentication successful for email: {}", email);
            return token;
        } catch (Exception e) {
            logger.error("Authentication error for email: {} - Error: {}", email, e.getMessage());
            throw e;
        }
    }

    public UserResponse registerUser(RegisterRequest request) {
        try {
            logger.info("Registering new user with email: {}", request.getEmail());

            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                logger.warn("Registration failed - Email already exists: {}", request.getEmail());
                throw new RuntimeException("Email already registered");
            }

            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER);

            // Generate 4-digit verification code
            String verificationCode = RandomStringUtils.randomNumeric(4);
            user.setVerificationCode(verificationCode);
            user.setVerified(false);

            User savedUser = userRepository.save(user);
            logger.info("User registered successfully with ID: {}", savedUser.getId());

            // Send verification email
            sendVerificationEmail(savedUser.getEmail(), verificationCode);

            UserResponse userResponse = new UserResponse();
            userResponse.setId(savedUser.getId());
            userResponse.setEmail(savedUser.getEmail());

            return userResponse;
        } catch (Exception e) {
            logger.error("Registration failed for email: {} - Error: {}", request.getEmail(), e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private void sendVerificationEmail(String email, String verificationCode) {
        try {
            String subject = "Email Verification";
            String content = "Your verification code is: " + verificationCode
                    + "\n\nPlease use this code to verify your account.";

            emailService.sendEmail(email, subject, content);
            logger.info("Verification email sent to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send verification email to: {} - Error: {}", email, e.getMessage());
            throw new RuntimeException("Failed to send verification email");
        }
    }
}