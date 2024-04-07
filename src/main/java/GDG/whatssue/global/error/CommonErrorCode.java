package GDG.whatssue.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Invalid Input Value"),
    OAUTH_ERROR(HttpStatus.BAD_REQUEST, "OAuth Error"),
    FORBIDDEN_ACCESS_ERROR(HttpStatus.FORBIDDEN, "You do not have access to this resource"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private final HttpStatus httpStatus;
    private final String Message;
}
