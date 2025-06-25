package hanh_engel.webplatform.service;

import hanh_engel.webplatform.dto.CountryResponse;
import hanh_engel.webplatform.entity.Country;
import hanh_engel.webplatform.entity.User;
import hanh_engel.webplatform.exception.ResourceNotFoundException;
import hanh_engel.webplatform.repository.UserRepository;
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

    @Transactional
    public CountryResponse selectCountry(String email, Country country) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email ist erforderlich");
        }

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