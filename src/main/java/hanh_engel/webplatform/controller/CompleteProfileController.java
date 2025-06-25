package hanh_engel.webplatform.controller;

import hanh_engel.webplatform.dto.ApiResponse;
import hanh_engel.webplatform.dto.ProfileResponseDto;
import hanh_engel.webplatform.dto.UpdateProfileDto;
import hanh_engel.webplatform.service.CompleteProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CompleteProfileController {

    @Autowired
    private CompleteProfileService completeProfileService;

    @PostMapping(value = "/complete-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProfileResponseDto>> completeProfile(
            @RequestPart("email") String email,  // Only changed this annotation
            @ModelAttribute @Valid UpdateProfileDto request
    ) {
        // Keep all existing logic
        ProfileResponseDto profileResponseDto = completeProfileService.completeProfile(email, request);
        return ResponseEntity.ok(ApiResponse.success("Profile completed", profileResponseDto));
    }
}
