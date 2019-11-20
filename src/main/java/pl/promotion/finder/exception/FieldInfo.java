package pl.promotion.finder.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FieldInfo extends Throwable {
    private String name;
    private String message;
    private ErrorCode errorCode;

    public FieldInfo(String name, ErrorCode errorCode) {
        this.name = name;
        this.errorCode = errorCode;
    }
}
