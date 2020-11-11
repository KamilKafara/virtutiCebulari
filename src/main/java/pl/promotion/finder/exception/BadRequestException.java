package pl.promotion.finder.exception;

import lombok.Getter;

import java.util.Collection;
import java.util.Collections;

@Getter
public class BadRequestException extends RuntimeException {

    private Collection<FieldInfo> fields;

    public BadRequestException(String message, FieldInfo fieldInfo) {
        super(message);
        this.fields = Collections.singletonList(fieldInfo);
    }

}
