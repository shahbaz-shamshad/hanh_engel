package hanh_engel.webplatform.controller;

import hanh_engel.webplatform.dto.ApiResponse;
import hanh_engel.webplatform.dto.VerifyEmailRequest;
import hanh_engel.webplatform.service.VerifyEmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VerifyEmailController {

    private static final Logger logger = LogManager.getLogger(VerifyEmailController.class);

    @Autowired
    private VerifyEmailService verifyEmailService;

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestBody VerifyEmailRequest request) {
        try {
            logger.info("Verifying email for: {} with code: {}", request.getEmail(), request.getCode());
            verifyEmailService.verifyEmail(request.getEmail(), request.getCode());
            logger.info("Email verified successfully for: {}", request.getEmail());
            return ResponseEntity.ok(ApiResponse.success("Email verified successfully"));
        } catch (Exception e) {
            logger.error("Email verification failed for: {} - Error: {}",
                    request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to verify email: " + e.getMessage()));
        }
    }
}