package GDG.whatssue.domain.member.controller;

import GDG.whatssue.domain.member.dto.*;
import GDG.whatssue.domain.member.service.ClubMemberManagingService;
import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginUser;
import GDG.whatssue.global.common.annotation.SkipFirstVisitCheck;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}")
public class ClubMemberController {
    private final ClubMemberService clubMemberService;
    private final ClubMemberManagingService clubMemberManagingService;

    @ClubManager
    @DeleteMapping("/member/{memberId}/manager")
    @Operation(summary = "멤버 추방", description = "멤버를 클럽에서 추방합니다.(매니저만 이용 가능한 기능)")
    public ResponseEntity deleteMember(@PathVariable Long clubId, @PathVariable Long memberId) {
        clubMemberManagingService.deleteClubMember(memberId);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @ClubManager
    @PatchMapping ("/member/{memberId}/manager")
    @Operation(summary = "멤버 권한 수정", description = "멤버의 권한을 수정합니다. role 은 string 형태로 'member' or 'manager'와 같이 입력해야 합니다.(대소문자 구분 x)")
    public ResponseEntity modifyMemberRole(@PathVariable Long clubId, @PathVariable Long memberId, @RequestParam("role") String role) {
        clubMemberManagingService.modifyClubMemberRole(memberId, role);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @GetMapping("/member/info")
    @Operation(summary = "유저의 클럽 내 멤버 아이디 및 역할 조회")
    public ResponseEntity getMemberInfo(@PathVariable Long clubId, @LoginUser Long userId) {
        ClubMemberDto dto = clubMemberService.getMemberIdAndRole(clubId, userId);
        return new ResponseEntity(dto, HttpStatus.OK);
    }


    @PatchMapping("/member/{memberId}")
    @Operation(summary = "멤버 정보 수정")
    public ResponseEntity modifyMemberInfo(@PathVariable Long memberId, ClubMemberInfoDto dto) {
        clubMemberService.modifyClubMember(memberId,dto);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @SkipFirstVisitCheck
    @PostMapping(value = "/member/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary ="멤버 프로필 등록")
    public ResponseEntity<Void> setProfile(
            @LoginUser Long userId,
            @PathVariable Long clubId,
            @Valid @ModelAttribute CreateMemberProfileRequest request) throws IOException {
        clubMemberService.setMemberProfile(clubId, userId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/member/profile")
    @Operation(summary = "프로필 조회 ( 멤버 + 유저 )")
    public ResponseEntity getProfile(@PathVariable Long clubId, @LoginUser Long userId){
        MemberProfileDto response = clubMemberService.getMemberProfile(clubId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/member/auth")
    public ResponseEntity<MemberAuthInfoResponse> getMemberAuthInfo(@PathVariable Long clubId, @LoginUser Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(clubMemberService.getMemberAuthInfo(clubId, userId));
    }
}