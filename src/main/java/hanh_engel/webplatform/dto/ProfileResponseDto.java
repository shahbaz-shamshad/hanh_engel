package hanh_engel.webplatform.dto;

import hanh_engel.webplatform.entity.User;
import lombok.Data;

@Data
public class ProfileResponseDto {

    private String image;
    private String infoA;
    private String infoB;

    public ProfileResponseDto(String email, String infoA, String infoB, String image) {
    }

    public ProfileResponseDto() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInfoA() {
        return infoA;
    }

    public void setInfoA(String infoA) {
        this.infoA = infoA;
    }

    public String getInfoB() {
        return infoB;
    }

    public void setInfoB(String infoB) {
        this.infoB = infoB;
    }
}
