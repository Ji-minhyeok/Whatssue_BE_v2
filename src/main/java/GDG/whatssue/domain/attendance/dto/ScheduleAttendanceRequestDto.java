package GDG.whatssue.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleAttendanceRequestDto {
    private Long clubMemberId;
    private int attendanceNum;

}
