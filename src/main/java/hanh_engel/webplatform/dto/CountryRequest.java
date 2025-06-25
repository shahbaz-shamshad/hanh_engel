
package hanh_engel.webplatform.dto;

import hanh_engel.webplatform.entity.Country;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class CountryRequest {
    private Country country;
    private String email;
}