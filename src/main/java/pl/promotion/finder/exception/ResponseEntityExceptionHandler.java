package pl.promotion.finder.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Log4j2
@ControllerAdvice
public class ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ResponseErrorInfo> handleNotFoundException(NotFoundException ex) {
        ResponseErrorInfo errorInfo = ResponseErrorInfo.builder()
                .message(ex.getMessage())
                .fields(ex.getFields())
                .build();
        log.error(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorInfo);
    }
}
