package hanh_engel.webplatform.controller;

import hanh_engel.webplatform.dto.ApiResponse;
import hanh_engel.webplatform.service.LogoutService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LogoutController {
    private static final Logger logger = LogManager.getLogger(LogoutController.class);

    @Autowired
    private LogoutService logoutService;

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "").trim();
            logger.info("Processing logout request for token: {}", token);

            logoutService.logout(token);

            logger.info("Logout successful for token: {}", token);
            return ResponseEntity.ok(ApiResponse.success("Logout successful"));
        } catch (Exception e) {
            logger.error("Logout failed - Error: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Logout failed: " + e.getMessage()));
        }
    }
}