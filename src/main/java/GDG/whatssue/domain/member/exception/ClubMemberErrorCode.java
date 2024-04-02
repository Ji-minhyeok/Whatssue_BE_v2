package GDG.whatssue.domain.member.exception;

import GDG.whatssue.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClubMemberErrorCode implements ErrorCode {

    CLUB_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "No Club With That Club Code"),
    INVALID_PRIVATE_CODE_PATTERN_ERROR(HttpStatus.BAD_REQUEST, "Invalid Private Code Pattern [FFFFFF]"),
    INVALID_PRIVATE_CODE_ERROR(HttpStatus.BAD_REQUEST, "Invalid Private Code"),
    DUPLICATE_CLUB_JOIN_ERROR(HttpStatus.BAD_REQUEST, "Club That Has Already Joined"),
    PRIVATE_CLUB_ERROR(HttpStatus.BAD_REQUEST, "Private Club Needs Private Code"),
    CLUB_MEMBER_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "No Club Member With That Member Id"),
    CLUB_MEMBER_COULD_NOT_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Club Member Cannot be Not Deleted" ),
    CLUB_MEMBER_COULD_NOT_MODIFY_ERROR(HttpStatus.NOT_MODIFIED, "Club Member's Role Was Not Modified" );

    private final HttpStatus httpStatus;
    private final String message;
}
