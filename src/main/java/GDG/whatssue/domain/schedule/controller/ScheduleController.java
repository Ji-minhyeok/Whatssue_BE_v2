package GDG.whatssue.domain.schedule.controller;

import GDG.whatssue.domain.schedule.dto.AddScheduleRequest;
import GDG.whatssue.domain.schedule.dto.GetScheduleResponse;
import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
import GDG.whatssue.domain.schedule.exception.ScheduleErrorCode;
import GDG.whatssue.domain.schedule.service.ScheduleService;
import GDG.whatssue.global.error.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{clubId}/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * Filter 또는 Interceptor를 통해 schedule이 club의 것인지 체크 필요 TODO
     */

    @Operation(summary = "일정 추가", description = "날짜 패턴 yyyy-MM-dd HH:ss")
    @PostMapping
//    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity addSchedule(@PathVariable(name = "clubId") Long clubId, @Valid @RequestBody AddScheduleRequest requestDto) {

        scheduleService.saveSchedule(clubId, requestDto);

        return ResponseEntity.status(200).body("ok");
    }

    @Operation(summary = "일정 수정", description = "날짜 패턴 yyyy-MM-dd HH:ss")
    @PatchMapping("/{scheduleId}")
//    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity modifySchedule(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId,
        @Valid @RequestBody ModifyScheduleRequest requestDto) {

        scheduleService.updateSchedule(scheduleId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @Operation(summary = "일정 삭제")
    @DeleteMapping("/{scheduleId}")
//    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity deleteSchedule(@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
    
    @Operation(summary = "일정 상세조회")
    @GetMapping("/{scheduleId}")
//    @PreAuthorize("hasAnyRole('ROLE_'+#clubId+'MANAGER','ROLE_'+#clubId+'MEMBER')")
    public ResponseEntity getSchedule (@PathVariable(name = "clubId") Long clubId, @PathVariable(name = "scheduleId") Long scheduleId) {

        GetScheduleResponse scheduleDto = scheduleService.findSchedule(scheduleId);

        return ResponseEntity.status(HttpStatus.OK).body(scheduleDto);
    }

    @Operation(summary = "일정 조회(전체/일별/월별)")
    @GetMapping
    @Parameter(name = "date", description = "날짜 미입력 시 전체 일정 조회 (날짜 패턴 : yyyy-MM-dd or yyyy-MM)", required = false, in = ParameterIn.QUERY)
//    @PreAuthorize("hasAnyRole('ROLE_'+#clubId+'MANAGER','ROLE_'+#clubId+'MEMBER')")
    public ResponseEntity getScheduleAll( @PathVariable(name = "clubId") Long clubId, @RequestParam(name = "date", required = false) String date) {

        List<GetScheduleResponse> responseDtoList;

        if (date == null) { //전체 조회
            responseDtoList = scheduleService.findScheduleAll(clubId);
        } else {
            boolean day_check = Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", date);
            boolean month_check = Pattern.matches("[0-9]{4}-[0-9]{2}", date);

            if (day_check) { //일자별 조회
                responseDtoList = scheduleService.findScheduleByDay(clubId, date);
            } else if (month_check) { //월별 조회
                responseDtoList = scheduleService.findScheduleByMonth(clubId, date);
            } else { //지정 패턴과 맞지 않음
                throw new CommonException(ScheduleErrorCode.INVALID_SCHEDULE_DATE_ERROR);
            }
        }

        return new ResponseEntity(responseDtoList, HttpStatus.OK);
    }
}