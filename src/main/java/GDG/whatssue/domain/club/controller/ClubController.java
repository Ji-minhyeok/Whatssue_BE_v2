package GDG.whatssue.domain.club.controller;

import GDG.whatssue.domain.club.dto.ClubCreateRequest;
import GDG.whatssue.domain.club.dto.ClubCreateResponse;
import GDG.whatssue.domain.club.dto.GetClubInfoResponse;
import GDG.whatssue.domain.club.dto.GetJoinClubListResponse;
import GDG.whatssue.domain.club.dto.UpdateClubInfoRequest;
import GDG.whatssue.domain.club.service.ClubService;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 가입한 모임 조회 : [GET] - /api/clubs
 * 모임 생성 : [POST] - /api/clubs
 * 모임 정보 수정  [PATCH] - /api/clubs/{clubId}/info
 * 모임 삭제 : [DELETE] - /api/clubs/{clubId} TODO
 * 모임 초대코드 갱신 : [PATCH] - /api/clubs/{clubId}/private-code
 * 모임 가입신청 활성/비활성화 : [PATCH] - /api/clubs/{clubId}/private
 * 모임 정보 조회 : [GET] - /api/clubs/{clubId}/info
 */

@Tag(name = "ClubController", description = "모임 생성/수정 등 모임에 관련된 api")
@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;
    
    @Operation(summary = "가입한 모임 조회")
    @GetMapping
    public ResponseEntity getJoinClubList(@LoginUser Long userId) {
        List<GetJoinClubListResponse> responseDto = clubService.getJoinClubList(userId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "모임 생성")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity createClub(
        @LoginUser Long userId,
        @RequestPart("request") ClubCreateRequest request,
        @RequestPart("profileImage") MultipartFile profileImage) throws IOException {

        //Validation 및 예외처리 TODO

        ClubCreateResponse responseDto = clubService.createClub(userId, request, profileImage);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @ClubManager
    @Operation(summary = "모임 정보 수정", description = "최종 프로필 사진이 기본 사진일 시 profileImage 헤더 x")
    @PatchMapping(value = "/{clubId}/info",
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity updateClubInfo(@PathVariable("clubId") Long clubId,
        @RequestPart("request") UpdateClubInfoRequest request,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {

        clubService.updateClubInfo(clubId, request, profileImage);

        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @ClubManager
    @Operation(summary = "초대코드 갱신")
    @PatchMapping("/{clubId}/private-code")
    public ResponseEntity updateClubPrivateCode(@PathVariable("clubId") Long clubId) {
        clubService.updateClubCode(clubId);

        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @ClubManager
    @Operation(summary = "모임 가입 신청 여닫기")
    @PatchMapping(value = "/{clubId}/private")
    public ResponseEntity updateClubPrivateStatus(@PathVariable("clubId") Long clubId){
        clubService.updateClubPrivateStatus(clubId);

        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @Operation(summary = "모임 정보 조회")
    @GetMapping("/{clubId}/info")
    public ResponseEntity getClubInfo(@PathVariable("clubId") Long clubId) {
        GetClubInfoResponse responseDto = clubService.getClubInfo(clubId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

//    @DeleteMapping
//    public ResponseEntity deleteClub() {
//        //TODO
//        return null;
//    }
}
