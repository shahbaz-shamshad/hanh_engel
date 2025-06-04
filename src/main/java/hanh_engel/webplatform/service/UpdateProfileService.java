package hanh_engel.webplatform.service;

import hanh_engel.webplatform.dto.UpdateProfileDto;
import hanh_engel.webplatform.entity.User;
import hanh_engel.webplatform.repository.UserRepository;
import hanh_engel.webplatform.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class UpdateProfileService {

    private static final Logger logger = LogManager.getLogger(UpdateProfileService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public User updateProfile(String token, UpdateProfileDto dto) {
        try {
            logger.debug("Processing profile update for token: {}", token);


            if (!jwtUtil.validateToken(token)) {
                logger.warn("Profile update failed - Invalid or expired token: {}", token);
                throw new AccessDeniedException("Invalid or expired token");
            }

            String email = jwtUtil.extractEmail(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.warn("Profile update failed - User not found for email: {}", email);
                        return new IllegalArgumentException("User not found");
                    });

            if (dto.getImage() != null && !dto.getImage().isEmpty()) {

                if (dto.getImage() == null) {
                    logger.error("Image is null");
                } else {
                    logger.info("Image name: {}", dto.getImage().getOriginalFilename());
                }

                MultipartFile image = dto.getImage();
                String originalFileName = image.getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + originalFileName;

                String uploadDir = Paths.get("uploads", "images").toAbsolutePath().toString();
                Path uploadPath = Paths.get(uploadDir);

                try {
                    if (!Files.exists(uploadPath)) {
                        try {
                            Files.createDirectories(uploadPath);
                        }catch (IOException e) {
                            logger.error("Error creating upload directory: {}", e.getMessage());
                            throw new RuntimeException("Error creating upload directory");
                        }
                    }

                    Path filePath = uploadPath.resolve(fileName);

                    Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    user.setImage(fileName);

                } catch (IOException e) {
                    logger.error("Error while saving image: {}", e.getMessage());
                    throw new RuntimeException("Error while saving image");
                }
            }



            if (dto.getInfoA() != null) {
                logger.debug("Updating infoA for user: {}", email);
                user.setInfoA(dto.getInfoA());
            }
            if (dto.getInfoB() != null) {
                logger.debug("Updating infoB for user: {}", email);
                user.setInfoB(dto.getInfoB());
            }

            User savedUser = userRepository.save(user);
            logger.info("Profile updated successfully for user: {}", email);
            return savedUser;
        } catch (Exception e) {
            logger.error("Profile update failed for token: {} - Error: {}", token, e.getMessage());
            throw e;
        }
    }

    private String saveUploadedFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();


        Path uploadDir = Paths.get("uploads/images").toAbsolutePath().normalize();


        Files.createDirectories(uploadDir);


        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}