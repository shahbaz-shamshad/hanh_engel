// hanh_engel/webplatform/service/CountryService.java
package hanh_engel.webplatform.service;

import hanh_engel.webplatform.dto.CountryResponse;
import hanh_engel.webplatform.entity.Country;
import hanh_engel.webplatform.entity.User;
import hanh_engel.webplatform.exception.GlobalExceptionHandler;
import hanh_engel.webplatform.exception.ResourceNotFoundException;
import hanh_engel.webplatform.repository.UserRepository;
import hanh_engel.webplatform.security.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountryService {

    private static final Logger logger = LogManager.getLogger(CountryService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public CountryResponse selectCountry(String token, Country country) {
        if (!jwtUtil.validateToken(token)) {
            logger.warn("Country selection failed - Invalid token");
            throw new SecurityException("Ungültiges Token");
        }

        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("User not found for email: {}", email);
                    return new ResourceNotFoundException("Benutzer nicht gefunden");
                });

        if (user.getCountry() != null) {
            logger.warn("Country already selected for user: {}", email);
            throw new IllegalStateException("Land wurde bereits ausgewählt");
        }

        user.setCountry(country);
        userRepository.save(user);

        logger.info("Country {} selected for user: {}", country, email);


        return new CountryResponse(
                getGermanCountryName(country),
                "Ihr Land wurde erfolgreich gespeichert"
        );
    }


    private String getGermanCountryName(Country country) {
        switch(country) {
            case DEUTSCHLAND: return "Deutschland";
            case USA: return "USA";
            case VIETNAM: return "Vietnam";
            case GROSSBRITANNIEN: return "Großbritannien";
            case SPANIEN: return "Spanien";
            case SCHWEIZ: return "Schweiz";
            default: return country.name();
        }
    }
}