package GDG.whatssue.global.error;

import GDG.whatssue.global.error.ErrorResult.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
        public ResponseEntity<ErrorResult> commonExHandle(CommonException e, HttpServletRequest request) {
        log.warn("common exception= {}", e.getErrorCode().name());
        ErrorCode errorCode = e.getErrorCode();
        ErrorResult errorResult = ErrorResult.builder()
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }

    //valid 검증 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        log.warn("exception=", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        ErrorCode errorCode = CommonErrorCode.BAD_REQUEST;

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<ValidationError> validationErrorList = fieldErrors.stream()
            .map(f -> ValidationError.of(f))
            .collect(Collectors.toList());

        ErrorResult errorResult = ErrorResult.builder()
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .path(request.getRequestURI())
            .errors(validationErrorList)
            .build();

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> allExHandle(Exception e, HttpServletRequest request) {
        log.warn("exception= {}", e.getMessage());
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResult errorResult = ErrorResult.builder()
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .path(request.getRequestURI())
            .build();


        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }
}
