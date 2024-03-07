package GDG.whatssue.domain.schedule.exception;

import GDG.whatssue.domain.schedule.controller.ScheduleController;
import GDG.whatssue.global.error.ErrorCode;
import GDG.whatssue.global.error.ErrorResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {ScheduleController.class})
public class ScheduleExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResult> scheduleDateTimePatternExHandle(HttpMessageNotReadableException e, HttpServletRequest request) {
        ScheduleErrorCode errorCode = ScheduleErrorCode.INVALID_SCHEDULE_DATETIME_ERROR;
        ErrorResult errorResult = ErrorResult.builder()
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorResult, errorCode.getHttpStatus());
    }
}
