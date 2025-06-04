package hanh_engel.webplatform.controller;

import hanh_engel.webplatform.dto.ApiResponse;
import hanh_engel.webplatform.dto.CountryRequest;
import hanh_engel.webplatform.dto.CountryResponse;
import hanh_engel.webplatform.service.CountryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CountryController {

    private static final Logger logger = LogManager.getLogger(CountryController.class);

    @Autowired
    private CountryService countryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CountryResponse>> selectCountry(
            @RequestBody CountryRequest request) {

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            logger.warn("Missing email in country selection request");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email ist erforderlich"));
        }

        try {
            logger.info("Processing country selection for email: {}", request.getEmail());

            CountryResponse response = countryService.selectCountry(
                    request.getEmail(),
                    request.getCountry()
            );

            logger.info("Country selected successfully for email: {}", request.getEmail());

            return ResponseEntity.ok(ApiResponse.success(
                    "Land erfolgreich ausgewählt",
                    response
            ));
        } catch (Exception e) {
            logger.error("Country selection failed for email {}: {}",
                    request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}