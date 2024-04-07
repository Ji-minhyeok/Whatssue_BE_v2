package GDG.whatssue.domain.schedule.service;

import GDG.whatssue.domain.schedule.dto.AddScheduleRequest;
import GDG.whatssue.domain.schedule.dto.GetScheduleResponse;
import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
import java.util.List;

public interface ScheduleService {
    void saveSchedule(Long clubId, AddScheduleRequest requestDto);
    void updateSchedule(Long scheduleId, ModifyScheduleRequest requestDto);
    void deleteSchedule(Long scheduleId);
    GetScheduleResponse findSchedule(Long scheduleId);
    List<GetScheduleResponse> findScheduleAll(Long clubId);
    List<GetScheduleResponse> findScheduleByDay(Long clubId, String date);
    List<GetScheduleResponse> findScheduleByMonth(Long clubId, String date);
    boolean isClubSchedule(Long clubId, Long scheduleId);
}
