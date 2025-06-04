package hanh_engel.webplatform.util;

import java.util.Random;

public class VerificationCodeGenerator {

    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // Generates a random 4-digit number
        return String.valueOf(code);
    }
}