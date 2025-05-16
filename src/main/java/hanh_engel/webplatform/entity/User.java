package hanh_engel.webplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Entity
@Data
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        @Size(max = 100, message = "Email must be less than 100 characters")
        private String email;

        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[&#$%^&+=])(?=\\S+$).{8,}$",
                message = "Password must contain 1 digit, 1 lowercase, 1 uppercase, 1 special character, and no spaces"
        )
        private String password;


        @Size(max = 255, message = "Image URL must be less than 255 characters")
        private String image;

        @Size(max = 100, message = "Info A must be less than 100 characters")
        private String infoA;

        @Size(max = 100, message = "Info B must be less than 100 characters")
        private String infoB;

        @Size(min = 4, max = 4, message = "Verification code must be 4 characters")
        private String verificationCode;

        private boolean verified;

        @Enumerated(EnumType.STRING)
        private Role role = Role.USER;

        @Enumerated(EnumType.STRING)
        private Country country;

}