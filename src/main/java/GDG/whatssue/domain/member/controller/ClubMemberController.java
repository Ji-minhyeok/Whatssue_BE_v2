package GDG.whatssue.domain.member.controller;

import GDG.whatssue.domain.member.dto.ClubMemberDto;
import GDG.whatssue.domain.member.service.ClubMemberManagingService;
import GDG.whatssue.domain.member.service.ClubMemberSerivce;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 멤버 권한 수정 : [PATCH] - /api/clubs/{clubId}/members/{memberId}/manager
 * 멤버 추방 : [DELETE] - /api/clubs/{clubId}/members/{memberId}/manager
 * 멤버 상세조회(일반멤버) : [GET] - /api/clubs/{clubId}/members/{memberId}
 * 멤버 상세조회(관리자) : [GET] - /api/clubs/{clubId}/members/{memberId}/manager
 * 멤버 목록 조회 : [GET] - /api/clubs/{clubId}/members
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/members")
public class ClubMemberController {
    private final ClubMemberSerivce clubMemberSerivce;
    private final ClubMemberManagingService clubMemberManagingService;
    @DeleteMapping("/{memberId}/manager")
    @PreAuthorize("hasAnyRole('ROLE_'+#clubId+'MANAGER')")
    @Operation(summary = "멤버 추방", description = "멤버를 클럽에서 추방합니다.(매니저만 이용 가능한 기능)")
    public ResponseEntity deleteMember(@PathVariable Long clubId, @PathVariable Long memberId) {
        clubMemberManagingService.deleteClubMember(memberId);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @PatchMapping ("/member/{memberId}/manager")
    @PreAuthorize("hasAnyRole('ROLE_'+#clubId+'MANAGER')")
    @Operation(summary = "멤버 권한 수정", description = "멤버의 권한을 수정합니다. role 은 string 형태로 'member' or 'manager'와 같이 입력해야 합니다.(대소문자 구분 x)")
    public ResponseEntity modifyMemberRole(@PathVariable Long clubId, @PathVariable Long memberId, @RequestParam("role") String role) {
        clubMemberManagingService.modifyClubMemberRole(memberId, role);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @PatchMapping("/{memberId}/member")
//    @PreAuthorize("hasAnyRole('ROLE_'+#clubId+'MEMBER','ROLE_'+#clubId+'MANAGER')")
    @Operation(summary = "멤버 정보 수정")
    public ResponseEntity modifyMemberInfo(@PathVariable Long memberId, ClubMemberDto dto) {
        clubMemberSerivce.modifyClubMember(memberId,dto);
        return new ResponseEntity("ok", HttpStatus.OK);
    }

}
