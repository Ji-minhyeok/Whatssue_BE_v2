package GDG.whatssue.global.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResult {

    private final String code;
    private final String message;
    private final String path;

    @JsonInclude(Include.NON_EMPTY)
    private final List<ValidationError> errors;

    @Getter
    @RequiredArgsConstructor
    static class ValidationError {
        private final String field;
        private final String message;

        static ValidationError of(FieldError fieldError) {
            return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
