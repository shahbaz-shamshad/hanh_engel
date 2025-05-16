// hanh_engel/webplatform/controller/CountryController.java
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
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CountryRequest request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Invalid or missing token in country selection request");
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Ungültiges oder fehlendes Token"));
        }

        try {
            String token = authHeader.substring(7);
            logger.info("Processing country selection for token: {}", token);

            CountryResponse response = countryService.selectCountry(token, request.getCountry());
            logger.info("Country selected successfully for token: {}", token);

            return ResponseEntity.ok(ApiResponse.success(
                    "Land erfolgreich ausgewählt",
                    response
            ));
        } catch (Exception e) {
            logger.error("Country selection failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}