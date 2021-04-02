package pl.promotion.finder.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FieldInfo extends Throwable {
    private final String name;
    private final ErrorCode errorCode;
    private String message;

    public FieldInfo(String name, ErrorCode errorCode) {
        this.name = name;
        this.errorCode = errorCode;
    }
}
