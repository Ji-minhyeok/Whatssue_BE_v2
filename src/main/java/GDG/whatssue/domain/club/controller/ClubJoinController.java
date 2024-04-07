package GDG.whatssue.domain.club.controller;

import GDG.whatssue.domain.club.dto.ClubJoinRequestDto;
import GDG.whatssue.domain.club.dto.ClubJoinRequestGetDto;
import GDG.whatssue.domain.club.dto.GetJoinRequestsResponse;
import GDG.whatssue.domain.club.service.ClubJoinService;
import GDG.whatssue.domain.club.service.MemberJoinService;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 모임 가입 신청 : [POST] - /api/join-requests
 * 신청 내역 조회 : [GET] - /api/join-requests
 * 신청 취소 : [DELETE] - /api/join-requests/{joinRequestId} TODO
 * 가입 요청 조회 : [GET] - /api/clubs/{clubId}/join-requests TODO
 * 가입 요청 수락 : [POST] - /api/clubs/{clubId}/join-requests/{clubJoinRequestId}/accept TODO
 * 가입 요청 거절 : [POST] - /api/club/{clubId}/join-requests/{clubJoinRequestId}/deny TODO
 **/

@Tag(name = "ClubJoinController", description = "유저의 모임가입과 가입요청 처리에 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClubJoinController {

    private final ClubJoinService clubJoinService;
    private final MemberJoinService memberJoinService;

    @Operation(summary = "모임가입 신청")
    @PostMapping("/join-requests")
    public ResponseEntity joinClub(@Valid @RequestBody ClubJoinRequestDto requestDto, @LoginUser Long userId) {

        //validation 예외처리 TODO
        clubJoinService.joinClub(userId, requestDto);

        return new ResponseEntity("ok", HttpStatus.OK);
    }

    @Operation(summary = "모임가입 신청내역 조회")
    @GetMapping("/join-requests")
    public ResponseEntity getJoinRequests(@LoginUser Long userId) {
        List<GetJoinRequestsResponse> responseDto = clubJoinService.getJoinRequests(userId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    @ClubManager
    @Operation(summary="멤버 가입요청 수락")
    @PostMapping("/clubs/{clubId}/join-requests/{clubJoinRequestId}/accept")
    public ResponseEntity acceptClubJoinRequest(@PathVariable Long clubJoinRequestId){
        memberJoinService.acceptResponse(clubJoinRequestId);

        return ResponseEntity.status(200).body("가입 신청 수락 완료");
    }

    @ClubManager
    @Operation(summary="멤버 가입요청 거절")
    @PostMapping("/clubs/{clubId}/join-requests/{clubJoinRequestId}/deny")
    public ResponseEntity denyClubJoinRequest(@PathVariable Long clubJoinRequestId){
        memberJoinService.denyResponse(clubJoinRequestId);

        return ResponseEntity.status(200).body("가입 신청 거절 완료");
    }

    @ClubManager
    @Operation(summary = "멤버 가입요청 목록 조회")
    @GetMapping("/clubs/{clubId}/join-requests")
    public ResponseEntity<List<ClubJoinRequestGetDto>> getClubJoinRequest(@PathVariable Long clubId){
        List<ClubJoinRequestGetDto>ClubJoinRequests = memberJoinService.getClubJoinRequests(clubId);

        return ResponseEntity.ok(ClubJoinRequests);
    }
}
