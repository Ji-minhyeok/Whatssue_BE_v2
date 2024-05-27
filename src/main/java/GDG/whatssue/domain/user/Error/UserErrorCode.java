package GDG.whatssue.domain.user.Error;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    CANNOT_MODIFY_OTHER_USER_INFO(HttpStatus.BAD_REQUEST, "Cannot modify other user info");

    private final HttpStatus httpStatus;
    private final String message;

}

