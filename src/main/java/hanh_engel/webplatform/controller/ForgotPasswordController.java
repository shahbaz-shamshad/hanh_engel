package hanh_engel.webplatform.controller;

import hanh_engel.webplatform.dto.*;
import hanh_engel.webplatform.service.ForgotPasswordService;
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
public class ForgotPasswordController {

    private static final Logger logger = LogManager.getLogger(ForgotPasswordController.class);

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            logger.info("Processing forgot password request for email: {}", request.getEmail());
            String message = forgotPasswordService.sendPasswordResetLink(request.getEmail());
            logger.info("Password reset link sent successfully to email: {}", request.getEmail());
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            logger.error("Failed to process forgot password request: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}