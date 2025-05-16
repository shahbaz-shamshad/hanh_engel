
package hanh_engel.webplatform.dto;

import lombok.Data;

@Data
public class CountryResponse {
    private String country;
    private String message;


    public CountryResponse(String country, String message) {
        this.country = country;
        this.message = message;
    }

    public CountryResponse() {
    }
}