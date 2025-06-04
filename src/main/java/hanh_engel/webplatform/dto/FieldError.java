package hanh_engel.webplatform.dto;

import lombok.Data;

@Data
public class FieldError {
    private String field;
    private String message;
    private Object rejectedValue;

    public FieldError(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }
}