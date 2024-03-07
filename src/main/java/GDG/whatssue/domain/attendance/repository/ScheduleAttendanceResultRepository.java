package GDG.whatssue.domain.attendance.repository;

import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleAttendanceResultRepository extends JpaRepository<ScheduleAttendanceResult, Long> {

    ScheduleAttendanceResult findByScheduleIdAndClubMemberId(Long scheduleId, Long clubMemberId);
    List<ScheduleAttendanceResult> findByScheduleId(Long scheduleId);


}
