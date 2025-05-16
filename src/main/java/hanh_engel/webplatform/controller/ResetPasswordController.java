package hanh_engel.webplatform.controller;

import hanh_engel.webplatform.dto.ApiResponse;
import hanh_engel.webplatform.dto.ResetPasswordRequest;
import hanh_engel.webplatform.service.ResetPasswordService;
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
public class ResetPasswordController {

    private static final Logger logger = LogManager.getLogger(ResetPasswordController.class);

    @Autowired
    private ResetPasswordService resetPasswordService;

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            logger.info("Processing password reset for token: {}", request.getToken());
            resetPasswordService.resetPassword(request.getToken(), request.getNewPassword());
            logger.info("Password reset successful for token: {}", request.getToken());
            return ResponseEntity.ok(ApiResponse.success("Password reset successful"));
        } catch (Exception e) {
            logger.error("Password reset failed for token: {} - Error: {}",
                    request.getToken(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to reset password: " + e.getMessage()));
        }
    }
}