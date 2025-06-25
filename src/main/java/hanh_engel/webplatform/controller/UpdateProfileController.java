package hanh_engel.webplatform.controller;

import hanh_engel.webplatform.dto.*;
import hanh_engel.webplatform.entity.User;
import hanh_engel.webplatform.service.CompleteProfileService;
import hanh_engel.webplatform.service.UpdateProfileService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UpdateProfileController {

    private static final Logger logger = LogManager.getLogger(UpdateProfileController.class);

    @Autowired
    private UpdateProfileService updateProfileService;



    @PutMapping("/update-profile")
    public ResponseEntity<ApiResponse<ProfileResponseDto>> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @ModelAttribute @Valid UpdateProfileDto request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Invalid or missing token in update profile request");
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Invalid or missing token"));
        }

        try {
            String token = authHeader.substring(7);
            logger.info("Processing profile update request for token: {}", token);

            User updatedUser = updateProfileService.updateProfile(token, request);
            logger.info("Profile updated successfully for token: {}", token);

            return ResponseEntity.ok(ApiResponse.success(
                    "Profile updated successfully",
                    new ProfileResponseDto()
            ));
        } catch (Exception e) {
            logger.error("Profile update failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }


    }



}