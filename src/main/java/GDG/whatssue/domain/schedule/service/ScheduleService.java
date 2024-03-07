package GDG.whatssue.domain.schedule.service;

import GDG.whatssue.domain.schedule.dto.AddScheduleRequest;
import GDG.whatssue.domain.schedule.dto.GetScheduleResponse;
import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.schedule.exception.ScheduleErrorCode;
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import GDG.whatssue.global.error.CommonException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * club에 유효한 schedule인지 체크. TODO
 */

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClubRepository clubRepository;

    public void saveSchedule(Long clubId, AddScheduleRequest requestDto) {
        Club club = clubRepository.findById(clubId).orElse(null);

        Schedule saveSchedule = requestDto.toEntity(club);
        scheduleRepository.save(saveSchedule);
    }

    @Transactional
    public void updateSchedule(Long scheduleId, ModifyScheduleRequest requestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(()-> new CommonException(ScheduleErrorCode.SCHEDULE_NOT_FOUND_ERROR));

        schedule.update(
            requestDto.getScheduleName(),
            requestDto.getScheduleContent(),
            requestDto.getScheduleDateTime());

        Schedule updatedSchedule = scheduleRepository.save(schedule);
    }

    /**
     * now HardDelete
     * SoftDelete TODO
     */
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(()-> new CommonException(ScheduleErrorCode.SCHEDULE_NOT_FOUND_ERROR));

        scheduleRepository.deleteById(scheduleId);
    }

    public GetScheduleResponse findSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(()-> new CommonException(ScheduleErrorCode.SCHEDULE_NOT_FOUND_ERROR));


        GetScheduleResponse scheduleDetailDto = GetScheduleResponse.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getScheduleName())
            .scheduleContent(schedule.getScheduleContent())
            .scheduleDateTime(schedule.getScheduleDateTime().toString()).build();

        return scheduleDetailDto;
    }

    public List<GetScheduleResponse> findScheduleAll(Long clubId) {
        Club findClub = clubRepository.findById(clubId).get();

        List<Schedule> scheduleList = findClub.getScheduleList();

        return ScheduleListToResponseDtoList(scheduleList, null, null);
    }

    public List<GetScheduleResponse> findScheduleByDay(Long clubId, String date) {
        Club findClub = clubRepository.findById(clubId).get();

        List<Schedule> scheduleList = findClub.getScheduleList();

        return ScheduleListToResponseDtoList(scheduleList, "yyyy-MM-dd", date);
    }

    public List<GetScheduleResponse> findScheduleByMonth(Long clubId, String date) {
        Club findClub = clubRepository.findById(clubId).get();

        List<Schedule> scheduleList = findClub.getScheduleList();

        return ScheduleListToResponseDtoList(scheduleList, "yyyy-MM", date);
    }

    public List<GetScheduleResponse> ScheduleListToResponseDtoList(List<Schedule> scheduleList, String pattern, String date) {

        if (pattern==null || date==null) { //전체 조회
            return scheduleList.stream()
                .map(s -> scheduleToGetScheduleResponse(s))
                .collect(Collectors.toList());
        } else { //필터링 조회
        return scheduleList.stream()
            .filter(s-> s.getScheduleDateTime().format(DateTimeFormatter.ofPattern(pattern)).equals(date))
            .map(s -> scheduleToGetScheduleResponse(s))
            .collect(Collectors.toList());
        }
    }

    public GetScheduleResponse scheduleToGetScheduleResponse(Schedule schedule) {
        return GetScheduleResponse.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getScheduleName())
            .scheduleContent(schedule.getScheduleContent())
            .scheduleDateTime(schedule.getScheduleDateTime().toString()).build();
    }
}

