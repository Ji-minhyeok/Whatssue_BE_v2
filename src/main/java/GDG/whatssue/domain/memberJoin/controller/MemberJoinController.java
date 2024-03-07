package GDG.whatssue.domain.memberJoin.controller;

import GDG.whatssue.domain.memberJoin.dto.ClubJoinRequestGetDto;
import GDG.whatssue.domain.memberJoin.service.MemberJoinService;
import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceGetRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{clubId}/member_join")
public class MemberJoinController {
    private final MemberJoinService memberJoinService;

    @PostMapping(value="accept/{clubJoinRequestId}")
    @Operation(summary="가입 신청 수락")
    public ResponseEntity acceptClubJoinRequest(@PathVariable Long clubJoinRequestId){
        memberJoinService.acceptResponse(clubJoinRequestId);
        return ResponseEntity.status(200).body("가입 신청 수락 완료");
    }
    @PostMapping(value="deny/{clubJoinRequestId}")
    @Operation(summary="가입 신청 거절")
    public ResponseEntity denyClubJoinRequest(@PathVariable Long clubJoinRequestId){
        memberJoinService.denyResponse(clubJoinRequestId);
        return ResponseEntity.status(200).body("가입 신청 거절 완료");
    }
    @GetMapping(value="list")
    @Operation(summary="가입 신청 리스트 조회")
    public ResponseEntity<List<ClubJoinRequestGetDto>> getClubJoinRequest(@PathVariable Long clubId){
        List<ClubJoinRequestGetDto>ClubJoinRequests = memberJoinService.getClubJoinRequests(clubId);
        return ResponseEntity.ok(ClubJoinRequests);

    }
    @DeleteMapping(value="delete")
    @Operation(summary="가입된 멤버 삭제")
    public ResponseEntity deleteClubMember(@RequestParam Long memberId,@PathVariable Long clubId){
        memberJoinService.deleteMember(clubId,memberId);
        return ResponseEntity.status(200).body("멤버 삭제 완료");
    }
}
