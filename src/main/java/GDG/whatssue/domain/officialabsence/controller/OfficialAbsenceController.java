package GDG.whatssue.domain.officialabsence.controller;

import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceAddRequestDto;
import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceGetRequestDto;
import GDG.whatssue.domain.officialabsence.service.OfficialAbsenceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{clubId}/official_absence")
public class OfficialAbsenceController {
    private final OfficialAbsenceService officialAbsenceService;

    @PostMapping(value="/request/{scheduleId}")
    @Operation(summary="공결 신청")
    @PreAuthorize("hasAnyRole('ROLE_'+#clubId+'MEMBER','ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity addOfficialAbsenceRequest(
        @PathVariable String clubId,
        @PathVariable Long scheduleId,
        @RequestBody OfficialAbsenceAddRequestDto officialAbsenceAddRequestDto) {

        officialAbsenceService.createOfficialAbsenceRequest(scheduleId, officialAbsenceAddRequestDto);
        return ResponseEntity.status(200).body("공결 신청 완료");

    }
    @GetMapping(value="/list")
    @Operation(summary="공결 신청, 내역 전체 조회(MANAGER)")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity<List<OfficialAbsenceGetRequestDto>> getAllOfficialAbsenceRequestList(@PathVariable String clubId){
        List<OfficialAbsenceGetRequestDto> officialAbsenceRequests = officialAbsenceService.getAllOfficialAbsenceRequests();
        return ResponseEntity.ok(officialAbsenceRequests);
    }
    @GetMapping(value="/request_list")
    @Operation(summary="공결 신청 현황 조회(MANAGER)")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity<List<OfficialAbsenceGetRequestDto>> getOfficialAbsenceRequestList(@PathVariable String clubId){
        List<OfficialAbsenceGetRequestDto> officialAbsenceRequests = officialAbsenceService.getOfficialAbsenceRequests();
        return ResponseEntity.ok(officialAbsenceRequests);
    }
    @GetMapping(value="/done_list")
    @Operation(summary="공결 신청 내역 조회(MANAGER)")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity<List<OfficialAbsenceGetRequestDto>> getDoneOfficialAbsenceRequestList(@PathVariable String clubId){
        List<OfficialAbsenceGetRequestDto> officialAbsenceRequests = officialAbsenceService.getDoneOfficialAbsenceRequests();
        return ResponseEntity.ok(officialAbsenceRequests);
    }
    @GetMapping(value="/detail/{officialAbsenceId}")
    @Operation(summary="공결 신청 상세 조회(MANAGER)")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity<OfficialAbsenceGetRequestDto> getOfficialAbsenceRequestDetail(@PathVariable String clubId, @PathVariable Long officialAbsenceId){
        OfficialAbsenceGetRequestDto officialAbsenceRequest = officialAbsenceService.getOfficialAbsenceRequestDetail(officialAbsenceId);
        return ResponseEntity.ok(officialAbsenceRequest);
    }
    @GetMapping(value="/done_list/{clubMemberId}")
    @Operation(summary="내 공결 신청 내역 조회(MEMBER)")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MEMBER')")
    public ResponseEntity<List<OfficialAbsenceGetRequestDto>> getMyDoneOfficialAbsenceRequestList(@PathVariable Long clubMemberId, @PathVariable String clubId){
        List<OfficialAbsenceGetRequestDto> officialAbsenceRequests = officialAbsenceService.getMyDoneOfficialAbsenceRequests(clubMemberId);
        return ResponseEntity.ok(officialAbsenceRequests);
    }
    @GetMapping(value="/list/{clubMemberId}")
    @Operation(summary="내 공결 신청 현황 조회(MEMBER)")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MEMBER')")
    public ResponseEntity<List<OfficialAbsenceGetRequestDto>> getMyOfficialAbsenceRequestList(@PathVariable Long clubMemberId, @PathVariable String clubId){
        List<OfficialAbsenceGetRequestDto> officialAbsenceRequests = officialAbsenceService.getMyOfficialAbsenceRequests(clubMemberId);
        return ResponseEntity.ok(officialAbsenceRequests);
    }
    @PostMapping(value="/accept/{officialAbsenceId}")
    @Operation(summary="공결 신청 수락")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity acceptOfficialAbsence(@PathVariable Long officialAbsenceId,@PathVariable String clubId){
        officialAbsenceService.acceptResponse(officialAbsenceId);
        return ResponseEntity.status(200).body("공결 신청 수락 완료");
    }
    @DeleteMapping(value="/delete/{officialAbsenceId}")
    @Operation(summary="내 공결 신청 취소")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MEMBER')")
    public ResponseEntity deleteOfficialAbsence(@PathVariable Long officialAbsenceId,@PathVariable Long clubId){
        officialAbsenceService.deleteOfficialAbsences(officialAbsenceId, clubId);
        return ResponseEntity.status(200).body("공결 신청 취소 완료");
    }
    @PostMapping(value="/deny/{officialAbsenceId}")
    @Operation(summary="공결 신청 거절")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity denyOfficialAbsence(@PathVariable Long officialAbsenceId,@PathVariable String clubId){
        officialAbsenceService.denyResponse(officialAbsenceId);
        return ResponseEntity.status(200).body("공결 신청 거절 완료");
    }
}