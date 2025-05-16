package hanh_engel.webplatform.controller;

import hanh_engel.webplatform.dto.ApiResponse;
import hanh_engel.webplatform.dto.ProfileResponseDto;
import hanh_engel.webplatform.dto.UpdateProfileDto;
import hanh_engel.webplatform.service.CompleteProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CompleteProfileController {

    @Autowired
    private CompleteProfileService completeProfileService;
    @PostMapping("/complete-profile")
    public ResponseEntity<ApiResponse<ProfileResponseDto>> completeProfile(
            @RequestParam("email")String email,
            @ModelAttribute @Valid UpdateProfileDto request
    ) {
        ProfileResponseDto profileResponseDto = completeProfileService.completeProfile(email, request);
        return ResponseEntity.ok(ApiResponse.success(
                "Profile completed",
                profileResponseDto
        ));
    }
}
