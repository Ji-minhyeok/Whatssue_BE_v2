package GDG.whatssue.domain.club.exception;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClubErrorCode implements ErrorCode {
    CLUB_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "Club Not Found"),
    INVALID_PRIVATE_CODE_PATTERN_ERROR(HttpStatus.BAD_REQUEST, "Invalid Private Code Pattern [FFFFFF]"),
    INVALID_PRIVATE_CODE_ERROR(HttpStatus.BAD_REQUEST, "Invalid Private Code"),
    DUPLICATE_CLUB_JOIN_ERROR(HttpStatus.BAD_REQUEST, "Club That Has Already Joined"),
    DUPLICATE_CLUB_JOIN_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "Club That Has Already Requested");

    private final HttpStatus httpStatus;
    private final String message;

}
