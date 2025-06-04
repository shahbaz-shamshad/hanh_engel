package hanh_engel.webplatform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDto {

    private MultipartFile image;

    @Size(max = 100, message = "Info A too long")
    private String infoA;

    @Size(max = 100, message = "Info B too long")
    private String infoB;

//    @Email(message = "Invalid email format")
//    private String email;
}