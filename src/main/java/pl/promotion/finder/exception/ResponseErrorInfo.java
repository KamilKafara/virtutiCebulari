package pl.promotion.finder.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
class ResponseErrorInfo {
    private String message;
    private Collection<FieldInfo> fields;
}
