package GDG.whatssue.domain.attendance.controller;

import GDG.whatssue.domain.attendance.dto.AttendanceNumResponseDto;
import GDG.whatssue.domain.attendance.dto.ScheduleAttendanceMemberDto;
import GDG.whatssue.domain.attendance.dto.ScheduleAttendanceRequestDto;
import GDG.whatssue.domain.attendance.dto.ScheduleDto;
import GDG.whatssue.domain.attendance.service.AttendanceService;
import GDG.whatssue.domain.schedule.entity.Schedule;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/api/{clubId}")
@RestController
@Slf4j
public class AttendanceController {
    private final AttendanceService attendanceService;

    @Operation(summary = "출석 열기_manager")
    @GetMapping("/schedules/{scheduleId}/attendance-start")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity openAttendance(@PathVariable Long clubId, @PathVariable Long scheduleId) {
        AttendanceNumResponseDto dto;
        try {
            dto = attendanceService.openAttendance(clubId, scheduleId);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @Operation(summary = "출석 종료")
    @PostMapping("/schedules/{scheduleId}/attendance-end")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity offAttendance(@PathVariable Long clubId, @PathVariable Long scheduleId) {
        try{
            attendanceService.deleteAttendance(clubId, scheduleId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @Operation(summary = "일정별 출석한 멤버 리스트 조회")
    @GetMapping("/schedules/{scheduleId}/attendance-list")
    @PreAuthorize("hasRole('ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity getAttendanceList( @PathVariable Long clubId, @PathVariable Long scheduleId) throws Exception {
        List<ScheduleAttendanceMemberDto> list =  attendanceService.getAttendanceList(scheduleId, clubId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @Operation(summary = "출석하기 _ user")
    @PostMapping("/schedules/{scheduleId}/attendance")
    @PreAuthorize("hasAnyRole('ROLE_'+#clubId+'MEMBER','ROLE_'+#clubId+'MANAGER')")
    public ResponseEntity doAttendance(@PathVariable Long clubId, @PathVariable Long scheduleId, @RequestBody ScheduleAttendanceRequestDto requestDto) {
        try {
            attendanceService.doAttendance(clubId, scheduleId, requestDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("출석이 완료되었습니다.");
    }

    @Operation(summary = "현재 출석 진행 중인 스케줄")
    @GetMapping("/attendance-list")
    public ResponseEntity currentAttendanceList(@PathVariable Long clubId) {
        List<ScheduleDto> list = attendanceService.currentAttendanceList(clubId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}



