package GDG.whatssue.domain.schedule.controller;

import GDG.whatssue.domain.schedule.dto.AddScheduleRequest;
import GDG.whatssue.domain.schedule.dto.GetScheduleDetailResponse;
import GDG.whatssue.domain.schedule.dto.GetScheduleListResponse;
import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
import GDG.whatssue.domain.schedule.exception.ScheduleErrorCode;
import GDG.whatssue.domain.schedule.service.impl.ScheduleServiceImpl;
import GDG.whatssue.global.common.annotation.ClubManager;
import GDG.whatssue.global.common.annotation.LoginMember;
import GDG.whatssue.global.error.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;

import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Interceptor
 *  - ClubCheckInterceptor : 클럽 유효성 체크, 멤버 여부 체크, 관리자 여부 체크
 *  - ScheduleCheckInterceptor : 스케줄 유효성 체크, 스케줄-클럽 권한 체크
 */
@Tag(name = "ScheduleController", description = "모임의 일정에 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/schedules")
public class ScheduleController {

    private final ScheduleServiceImpl scheduleService;

    @ClubManager
    @Operation(summary = "일정 추가", description = "날짜 패턴 yyyy-MM-dd HH:ss")
    @PostMapping
    public ResponseEntity addSchedule (
        @PathVariable(name = "clubId") Long clubId,
        @LoginMember Long memberId,
        @Valid @RequestBody AddScheduleRequest requestDto) {
        scheduleService.saveSchedule(clubId, memberId, requestDto);

        return ResponseEntity.status(200).body("ok");
    }

    @ClubManager
    @Operation(summary = "일정 수정", description = "날짜 패턴 yyyy-MM-dd HH:ss")
    @PatchMapping("/{scheduleId}")
    public ResponseEntity modifySchedule(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId,
        @Valid @RequestBody ModifyScheduleRequest requestDto) {

        scheduleService.updateSchedule(scheduleId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @ClubManager
    @Operation(summary = "일정 삭제")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity deleteSchedule(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
    
    @Operation(summary = "일정 상세조회")
    @GetMapping("/{scheduleId}")
    public ResponseEntity getSchedule (@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId) {
        GetScheduleDetailResponse scheduleDto = scheduleService.findSchedule(scheduleId);

        return ResponseEntity.status(HttpStatus.OK).body(scheduleDto);
    }

    @Operation(summary = "일정 조회(전체/일별/월별)")
    @GetMapping
    @Parameter(name = "date", description = "날짜 미입력 시 전체 일정 조회 (날짜 패턴 : yyyy-MM-dd or yyyy-MM)", required = false, in = ParameterIn.QUERY)
    public ResponseEntity getScheduleAll( @PathVariable(name = "clubId") Long clubId, @RequestParam(name = "date", required = false) String date) {
        List<GetScheduleListResponse> responseDtoList = getScheduleResponses(clubId, date);

        return new ResponseEntity(responseDtoList, HttpStatus.OK);
    }

    private List<GetScheduleListResponse> getScheduleResponses(Long clubId, String date) {
        List<GetScheduleListResponse> responseDtoList;
        if (date == null) { //전체 조회
            responseDtoList = scheduleService.findScheduleAll(clubId);
        } else {
            boolean day_check = Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", date);
            boolean month_check = Pattern.matches("[0-9]{4}-[0-9]{2}", date);

            responseDtoList = getScheduleResponsesByFilter(clubId, date, day_check, month_check);
        }

        return responseDtoList;
    }

    private List<GetScheduleListResponse> getScheduleResponsesByFilter(Long clubId, String date, boolean day_check, boolean month_check) {
        List<GetScheduleListResponse> responseDtoList;
        if (day_check) { //일자별 조회
            responseDtoList = scheduleService.findScheduleByDay(clubId, date);
        } else if (month_check) { //월별 조회
            responseDtoList = scheduleService.findScheduleByMonth(clubId, date);
        } else { //지정 패턴과 맞지 않음
            throw new CommonException(ScheduleErrorCode.INVALID_SCHEDULE_DATE_PATTERN_ERROR);
        }

        return responseDtoList;
    }
}