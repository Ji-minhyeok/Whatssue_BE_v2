package GDG.whatssue.domain.member.controller;

import GDG.whatssue.domain.member.exception.ClubMemberErrorCode;
import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.global.error.CommonException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClubMemberController {

    private final ClubMemberService clubMemberService;

    @PostMapping("/join/{clubCode}")
    public ResponseEntity joinClub(@PathVariable(name = "clubCode") String clubCode) {
        //현재 로그인 id parameter로 받아오기 TODO
        Long userId = 1L;

        boolean checkClubCode = Pattern.matches("[0-9]{6}", clubCode);

        if (checkClubCode) {
            clubMemberService.addClubJoinRequest(userId, clubCode);
            return new ResponseEntity("ok", HttpStatus.OK);
        } else {
            throw new CommonException(ClubMemberErrorCode.INVALID_CLUB_CODE_ERROR);
        }
    }
}
