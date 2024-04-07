package GDG.whatssue.domain.member.exception;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClubMemberErrorCode implements ErrorCode {
    CLUB_MEMBER_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "Club Member Not Found"),

    CLUB_MEMBER_COULD_NOT_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Club Member Cannot be Not Deleted" ),
    PROFILE_SETUP_REQUIRED_ERROR(HttpStatus.PRECONDITION_REQUIRED, "Initial profile settings required"),
    CLUB_MEMBER_COULD_NOT_MODIFY_ERROR(HttpStatus.NOT_MODIFIED, "Club Member's Role Was Not Modified");

    private final HttpStatus httpStatus;
    private final String message;
}
