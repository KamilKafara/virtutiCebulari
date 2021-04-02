package pl.promotion.finder.exception;

import lombok.Getter;

import java.util.Collection;
import java.util.Collections;

@Getter
public class NotFoundException extends RuntimeException {

    private final Collection<FieldInfo> fields;
    public NotFoundException(String message, FieldInfo fieldInfo) {
        super(message);
        this.fields = Collections.singletonList(fieldInfo);
    }

}
