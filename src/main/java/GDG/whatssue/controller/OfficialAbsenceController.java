package GDG.whatssue.controller;

import GDG.whatssue.dto.OfficialAbsence.OfficialAbsenceAddRequestDto;
import GDG.whatssue.dto.OfficialAbsence.OfficialAbsenceGetRequestDto;
import GDG.whatssue.service.OfficialAbsenceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{clubId}/official_absence")
public class OfficialAbsenceController {
    private final OfficialAbsenceService officialAbsenceService;

    @PostMapping(value="/request/{scheduleId}")@Operation(summary="공결 신청")
    public ResponseEntity addOfficialAbsenceRequest(
            @PathVariable String clubId,
            @PathVariable Long scheduleId,
            @RequestBody OfficialAbsenceAddRequestDto officialAbsenceAddRequestDto) {

        officialAbsenceService.createOfficialAbsenceRequest(scheduleId, officialAbsenceAddRequestDto);
        return ResponseEntity.status(200).body("공결 신청 완료");

    }
    @GetMapping(value="/list")@Operation(summary="공결 신청 전체 조회")
    public ResponseEntity<List<OfficialAbsenceGetRequestDto>> getOfficialAbsenceRequest(@PathVariable String clubId){
        List<OfficialAbsenceGetRequestDto> officialAbsenceRequests = officialAbsenceService.getOfficialAbsenceRequests();
        return ResponseEntity.ok(officialAbsenceRequests);
    }
    @PostMapping(value="/accept/{officialAbsenceId}")@Operation(summary="공결 신청 수락")
    public ResponseEntity acceptOfficialAbsence(@PathVariable Long officialAbsenceId,@PathVariable String clubId){
        officialAbsenceService.acceptResponse(officialAbsenceId);
        return ResponseEntity.status(200).body("공결 신청 수락 완료");
    }
    @PostMapping(value="/deny/{officialAbsenceId}")@Operation(summary="공결 신청 거절")
    public ResponseEntity denyOfficialAbsence(@PathVariable Long officialAbsenceId,@PathVariable String clubId){
        officialAbsenceService.denyResponse(officialAbsenceId);
        return ResponseEntity.status(200).body("공결 신청 거절 완료");
    }
}
