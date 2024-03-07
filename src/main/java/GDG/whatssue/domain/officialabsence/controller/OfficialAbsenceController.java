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
    @Operation(summary="공결 신청 전체 조회")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity<List<OfficialAbsenceGetRequestDto>> getOfficialAbsenceRequest(@PathVariable String clubId){
        List<OfficialAbsenceGetRequestDto> officialAbsenceRequests = officialAbsenceService.getOfficialAbsenceRequests();
        return ResponseEntity.ok(officialAbsenceRequests);
    }
    @PostMapping(value="/accept/{officialAbsenceId}")
    @Operation(summary="공결 신청 수락")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity acceptOfficialAbsence(@PathVariable Long officialAbsenceId,@PathVariable String clubId){
        officialAbsenceService.acceptResponse(officialAbsenceId);
        return ResponseEntity.status(200).body("공결 신청 수락 완료");
    }
    @PostMapping(value="/deny/{officialAbsenceId}")
    @Operation(summary="공결 신청 거절")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity denyOfficialAbsence(@PathVariable Long officialAbsenceId,@PathVariable String clubId){
        officialAbsenceService.denyResponse(officialAbsenceId);
        return ResponseEntity.status(200).body("공결 신청 거절 완료");
    }
}