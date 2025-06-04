package hanh_engel.webplatform.controller;

import hanh_engel.webplatform.dto.*;
import hanh_engel.webplatform.entity.User;
import hanh_engel.webplatform.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;



    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Attempting login for email: {}", loginRequest.getEmail());
            String token = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

            logger.info("Login successful for email: {}", loginRequest.getEmail());
            return ResponseEntity.ok(ApiResponse.success("Login successful", token));

        } catch (Exception e) {
            logger.error("Login failed for email: {} - Error: {}", loginRequest.getEmail(), e.getMessage());
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Invalid email or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest request) {
        try {
            logger.info("Registering new user with email: {}", request.getEmail());
            UserResponse userResponse = authService.registerUser(request);
            logger.info("User registered successfully with email: {}", request.getEmail());
            return ResponseEntity.ok(ApiResponse.success("Registration successful", userResponse));
        } catch (Exception e) {
            logger.error("Registration failed for email: {} - Error: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}