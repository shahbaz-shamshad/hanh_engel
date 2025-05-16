package hanh_engel.webplatform.service;

import hanh_engel.webplatform.dto.ProfileResponseDto;
import hanh_engel.webplatform.dto.UpdateProfileDto;
import hanh_engel.webplatform.entity.User;
import hanh_engel.webplatform.repository.UserRepository;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class CompleteProfileService {

    @Autowired
    private UserRepository userRepository;
    public ProfileResponseDto completeProfile(String email, UpdateProfileDto request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found with email: " + email));

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String fileName = saveUploadedFile(request.getImage());
                user.setImage(fileName);
            } catch (IOException | java.io.IOException e) {
                throw new RuntimeException("Error while saving image: " + e.getMessage());
            }
        }

        if (request.getInfoA() != null) {
            user.setInfoA(request.getInfoA());
        }
        if (request.getInfoB() != null) {
            user.setInfoB(request.getInfoB());
        }

        userRepository.save(user);

        return new ProfileResponseDto(user.getEmail(), user.getInfoA(), user.getInfoB(), user.getImage());
    }

    private String saveUploadedFile(MultipartFile file) throws java.io.IOException {
        String originalFileName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + originalFileName;

        String uploadDir = Paths.get("uploads", "images").toAbsolutePath().toString();
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
